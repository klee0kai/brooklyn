package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.charIndexInit() = apply {
    statement("if (charIndex) return 0")
    statement("charIndex = std::make_shared<SimpleIndexStructure>()")
    statement("charIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Character\") )")
    statement("charIndex->toJvm = env->GetMethodID(charIndex->cls, \"<init>\",\"(C)V\" ) ")
    statement("if (!charIndex->toJvm) return -1")
    statement("charIndex->fromJvm = env->GetMethodID(charIndex->cls, \"charValue\",\"()C\") ")
    statement("if (!charIndex->fromJvm) return -1")
}


fun Poet.mapCharFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<char> mapFromJChar(JNIEnv *env, jobject jValue) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jValue ? std::make_shared<char>( char(env->CallCharMethod(jValue, charIndex->fromJvm ))) ")
    statement(": std::shared_ptr<char>()")
    line("}")
}


fun Poet.mapCharToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJChar(JNIEnv *env, const std::shared_ptr<char>& valuePtr) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return valuePtr ? env->NewObject(charIndex->cls, charIndex->toJvm, jchar( *valuePtr ) ) : NULL")
    line("}")
}