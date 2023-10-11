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

fun CodeBuilder.mapJniClassApi(jClass: IrClass) = apply {
    body {
        val typeMirror = jClass.jniType()?.cppTypeMirrorStr ?: return@body

        lines(1)
        statement("std::shared_ptr<$typeMirror> mapFromJvm(JNIEnv *env, jobject j$typeMirror)")

        lines(1)
        statement("jobject mapToJvm(JNIEnv *env, std::shared_ptr<$typeMirror> j$typeMirror)")

        lines(1)
        statement("std::vector<$typeMirror> mapArrayFromJvm(JNIEnv *env, jobjectArray j$typeMirror)")

        lines(1)
        statement("jobjectArray mapArrayToJvm(JNIEnv *env, std::vector<$typeMirror> j$typeMirror)")
    }
}

fun CodeBuilder.mapJniClassImpl(jClass: IrClass) = apply {
    body {
        mapFromJvmImpl(jClass)
        mapToJvmImpl(jClass)
    }
}

private fun Poet.mapFromJvmImpl(jClass: IrClass) = apply {
    val jvmObjectName = "jvmObject"
    val cppObjectName = "cppObject"
    val indexClVariable = jClass.classId!!.indexVariableName
    val typeMirror = jClass.jniType()?.cppTypeMirrorStr

    lines(1)
    line("std::shared_ptr<$typeMirror> mapFromJvm(JNIEnv *env, jobject $jvmObjectName )")
    line("{")
    line("std::shared_ptr<$typeMirror> $cppObjectName = std::make_shared<$typeMirror>();")

    jClass.fields.forEach { field ->
        val fieldTypeMirror = field.type.jniType() ?: return@forEach

        val extractFromField = fieldTypeMirror.extractFromField.invoke(
            jvmObj = jvmObjectName,
            fieldOrMethodId = "${indexClVariable}->${field.name}"
        )

        post(" $cppObjectName->${field.name} = ")
        statement(fieldTypeMirror.transformToCppShort.invoke(extractFromField))
    }

    jClass.properties.forEach { property ->
        val propertyTypeMirror = property.getter?.returnType?.jniType() ?: return@forEach

        val extractFromProperty = propertyTypeMirror.extractFromMethod.invoke(
            jvmObj = jvmObjectName,
            fieldOrMethodId = "${indexClVariable}->${property.name}_getter"
        )
        post(" $cppObjectName->${property.name} = ")
        statement(propertyTypeMirror.transformToCppShort.invoke(extractFromProperty))

    }

    statement("return $cppObjectName")
    line("}")
    lines(1)
}


private fun Poet.mapToJvmImpl(jClass: IrClass) = apply {
    val jvmObjectName = "jvmObject"
    val cppObjectName = "cppObject"
    val indexClVariable = jClass.classId!!.indexVariableName
    val typeMirror = jClass.jniType()?.cppTypeMirrorStr

    lines(1)
    line("jobject mapToJvm(JNIEnv *env, std::shared_ptr<$typeMirror> $cppObjectName)")
    line("{")

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


