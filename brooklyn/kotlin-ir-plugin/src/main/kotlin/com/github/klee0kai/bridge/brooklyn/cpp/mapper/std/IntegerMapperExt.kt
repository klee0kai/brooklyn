package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.integerIndexInit() = apply {
    statement("if (integerIndex) return 0")
    statement("integerIndex = std::make_shared<SimpleIndexStructure>()")
    statement("integerIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Integer\") )")
    statement("integerIndex->toJvm = env->GetMethodID(integerIndex->cls, \"<init>\",\"(I)V\" ) ")
    statement("if (!integerIndex->toJvm) return -1")
    statement("integerIndex->fromJvm = env->GetMethodID(integerIndex->cls, \"intValue\",\"()I\") ")
    statement("if (!integerIndex->fromJvm) return -1")
}


fun Poet.mapIntegerFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJInteger(JNIEnv *env, jobject jInt) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jInt ? std::make_shared<int>( int(env->CallIntMethod(jInt, integerIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapIntegerToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJInteger(JNIEnv *env, const std::shared_ptr<int>& cppInt) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return cppInt ? env->NewObject(integerIndex->cls,integerIndex->toJvm, jint( *cppInt ) ) : NULL")

    line("}")
}