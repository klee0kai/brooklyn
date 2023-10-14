package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.fields
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.mapJniClass(jClass: IrClass, isImpl: Boolean = false) = apply {
    header {
        if (isImpl) {
            statement("using namespace std")
        }
    }
    body {
        mapFromJvm(jClass, isImpl)
        mapToJvm(jClass, isImpl)
        mapToJvmArray(jClass, isImpl)
        mapFromJvmArray(jClass, isImpl)
        mapToJvmArrayNullable(jClass, isImpl)
        mapFromJvmArrayNullable(jClass, isImpl)
    }
}

private fun Poet.mapFromJvm(jClass: IrClass, isImpl: Boolean = false) = apply {
    val jvmObjectName = "jvmObject"
    val cppObjectName = "cppObject"
    val indexClVariable = jClass.classId!!.indexVariableName
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply

    val declare = "std::shared_ptr<$typeMirror> mapFromJvm(JNIEnv *env, jobject $jvmObjectName)"
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!$jvmObjectName) return  shared_ptr<${typeMirror}>() ")
    line("shared_ptr<$typeMirror> $cppObjectName = make_shared<$typeMirror>();")

    jClass.fields.forEach { field ->
        val fieldTypeMirror = field.type.jniType() ?: return@forEach

        val extractFromField = fieldTypeMirror.extractFromField.invoke(
            jvmObj = jvmObjectName,
            fieldOrMethodId = "${indexClVariable}->${field.name}",
            args = "",
        )

        post(" $cppObjectName->${field.name} = ")
        statement(fieldTypeMirror.transformToCpp.invoke(extractFromField))
    }

    jClass.properties.forEach { property ->
        val propertyTypeMirror = property.getter?.returnType?.jniType() ?: return@forEach

        val extractFromProperty = propertyTypeMirror.extractFromMethod.invoke(
            jvmObj = jvmObjectName,
            fieldOrMethodId = "${indexClVariable}->${property.name}_getter",
            args = "",
        )
        post(" $cppObjectName->${property.name} = ")
        statement(propertyTypeMirror.transformToCpp.invoke(extractFromProperty))
    }

    statement("return $cppObjectName")
    line("}")
    lines(1)
}

private fun Poet.mapToJvm(jClass: IrClass, isImpl: Boolean = false) = apply {
    val jvmObjectName = "jvmObject"
    val cppObjectName = "cppObject"
    val indexClVariable = jClass.classId!!.indexVariableName
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply

    val declare = "jobject mapToJvm(JNIEnv *env, const std::shared_ptr<$typeMirror>& $cppObjectName)  "
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!$cppObjectName) return NULL")

    val constructor = jClass.constructors.first()
    if (constructor.fullValueParameterList.isNotEmpty()) {
        val arguments = constructor.fullValueParameterList.joinToString("\n,") { param ->
            val fieldTypeMirror = param.type.jniType() ?: return@joinToString ""
            fieldTypeMirror.transformToJni.invoke("${cppObjectName}->${param.name}")
        }

        statement("return env->NewObject(${indexClVariable}->cls,${indexClVariable}->${constructor.cppNameMirror}, ${arguments})")
    } else {
        statement("jobject $jvmObjectName = env->NewObject(${indexClVariable}->cls,${indexClVariable}->${constructor.cppNameMirror} )")
        jClass.fields.forEach { field ->
            val fieldTypeMirror = field.type.jniType() ?: return@forEach

            statement(
                fieldTypeMirror.insertToField.invoke(
                    variable = fieldTypeMirror.transformToJni.invoke("${cppObjectName}->${field.name}"),
                    jvmObj = jvmObjectName,
                    fieldOrMethodId = "${indexClVariable}->${field.name}"
                )
            )
        }

        jClass.properties.forEach { property ->
            val propertyTypeMirror = property.getter?.returnType?.jniType() ?: return@forEach

            val transformToJni = propertyTypeMirror.transformToJni.invoke("${cppObjectName}->${property.name}")
            statement(
                "env->CallVoidMethod(${jvmObjectName}, ${indexClVariable}->${property.name}_setter, ${transformToJni})"
            )
        }

        statement("return $jvmObjectName")
    }

    line("}")
    lines(1)
}


private fun Poet.mapToJvmArray(jClass: IrClass, isImpl: Boolean = false) = apply {
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply
    val indexClVariable = jClass.classId!!.indexVariableName
    val declare = "jobjectArray mapArrayToJvm(JNIEnv *env, const std::shared_ptr<std::vector<${typeMirror}>> &array)   "
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!array)return NULL")
    statement("jobjectArray jArray = env->NewObjectArray(array->size(), ${indexClVariable}->cls, NULL)")
    line("for (int i = 0; i < array->size(); i++) {")
    statement("env->SetObjectArrayElement(jArray, i, mapToJvm(env, make_shared<${typeMirror}>( (*array)[i] )))")
    line("}")
    statement("return jArray")
    line("}")
}

private fun Poet.mapToJvmArrayNullable(jClass: IrClass, isImpl: Boolean = false) = apply {
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply
    val indexClVariable = jClass.classId!!.indexVariableName
    val declare = "jobjectArray mapArrayNullableToJvm(JNIEnv *env, const std::shared_ptr<std::vector<std::shared_ptr<${typeMirror}>>> &array)   "
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!array)return NULL")
    statement("jobjectArray jArray = env->NewObjectArray(array->size(), ${indexClVariable}->cls, NULL)")
    line("for (int i = 0; i < array->size(); i++) {")
    statement("env->SetObjectArrayElement(jArray, i, mapToJvm(env, (*array)[i] ))")
    line("}")
    statement("return jArray")
    line("}")
}

private fun Poet.mapFromJvmArray(jClass: IrClass, isImpl: Boolean = false) = apply {
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply
    val declare =
        "std::shared_ptr<std::vector<${typeMirror}>> mapArrayFromJvm(JNIEnv *env, const jobjectArray &jarray )"
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!jarray)return {}")
    statement("int len = env->GetArrayLength(jarray)")
    statement("auto array = vector<${typeMirror}>(len)")
    line("for (int i = 0; i < len; i++) {")
    statement("array[i] = *mapFromJvm(env, env->GetObjectArrayElement(jarray, i))")
    line("}")
    statement("return make_shared<vector<${typeMirror}>>(array)")
    line("}")
}


private fun Poet.mapFromJvmArrayNullable(jClass: IrClass, isImpl: Boolean = false) = apply {
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply
    val declare =
        "std::shared_ptr<std::vector<std::shared_ptr<${typeMirror}>>> mapArrayNullableFromJvm(JNIEnv *env, const jobjectArray &jarray )"
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!jarray)return {}")
    statement("int len = env->GetArrayLength(jarray)")
    statement("auto array = vector<shared_ptr<${typeMirror}>>(len)")
    line("for (int i = 0; i < len; i++) {")
    statement("array[i] = mapFromJvm(env, env->GetObjectArrayElement(jarray, i))")
    line("}")
    statement("return make_shared<std::vector<shared_ptr<${typeMirror}>>>(array)")
    line("}")
}