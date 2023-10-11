package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.byteIndexInit() = apply {
    statement("if (byteIndex) return 0")
    statement("byteIndex = std::make_shared<SimpleIndexStructure>()")
    statement("byteIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java.lang.Byte\") )")
    statement("byteIndex->toJvm = env->GetMethodID(byteIndex->cls, \"<init>\",\"(B)V\" ) ")
    statement("if (!byteIndex->toJvm) return -1")
    statement("byteIndex->fromJvm = env->GetMethodID(byteIndex->cls, \"byteValue\",\"()B\") ")
    statement("if (!byteIndex->fromJvm) return -1")
}


fun Poet.mapByteFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJByte(JNIEnv *env, jobject jValue) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jValue ? std::make_shared<int>( int(env->CallShortMethod(jValue, byteIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapByteToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJByte(JNIEnv *env, const std::shared_ptr<int>& valuePtr) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return valuePtr ? env->NewObject(byteIndex->cls, byteIndex->toJvm, jbyte( *valuePtr ) ) : NULL")
    line("}")
}