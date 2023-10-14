package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId

fun CodeBuilder.mapMirrorClass(jClass: IrClass, isImpl: Boolean = false) = apply {
    header {
        if (isImpl) {
            statement("using namespace std")
        }
    }
    body {
        mapToJvmArray(jClass, isImpl)
        mapFromJvmArray(jClass, isImpl)
        mapToJvmArrayNullable(jClass, isImpl)
        mapFromJvmArrayNullable(jClass, isImpl)
    }
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
    statement("env->SetObjectArrayElement(jArray, i,  (*array)[i].jvmObject() )")
    line("}")
    statement("return jArray")
    line("}")
}

private fun Poet.mapToJvmArrayNullable(jClass: IrClass, isImpl: Boolean = false) = apply {
    val typeMirror = jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@apply
    val indexClVariable = jClass.classId!!.indexVariableName
    val declare =
        "jobjectArray mapArrayNullableToJvm(JNIEnv *env, const std::shared_ptr<std::vector<std::shared_ptr<${typeMirror}>>> &array)   "
    lines(1)
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!array)return NULL")
    statement("jobjectArray jArray = env->NewObjectArray(array->size(), ${indexClVariable}->cls, NULL)")
    line("for (int i = 0; i < array->size(); i++) {")
    statement("env->SetObjectArrayElement(jArray, i,  (*array)[i] ? (*array)[i]->jvmObject() : NULL )")
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
    statement("auto array = vector<${typeMirror}>()")
    line("for (int i = 0; i < len; i++) {")
    statement("array.push_back( ${typeMirror}( env->GetObjectArrayElement(jarray, i)) )")
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
    statement("auto array = vector<shared_ptr<${typeMirror}>>( len )")
    line("for (int i = 0; i < len; i++) {")
    statement("auto jItem =  env->GetObjectArrayElement(jarray, i)")
    statement("array[i] = jItem? make_shared<${typeMirror}>( jItem ) : shared_ptr<${typeMirror}>() ")
    line("}")
    statement("return make_shared<vector<shared_ptr<${typeMirror}>>>(array)")
    line("}")
}