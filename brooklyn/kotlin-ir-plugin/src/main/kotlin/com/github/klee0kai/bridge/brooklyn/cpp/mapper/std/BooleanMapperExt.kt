package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.booleanIndexInit() = apply {
    statement("if (booleanIndex) return 0")
    statement("booleanIndex = std::make_shared<BooleanIndex>()")
    statement("booleanIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java.lang.Boolean\") )")
    statement("booleanIndex->toJvm = env->GetMethodID(booleanIndex->cls, \"<init>\",\"(Z)V\" ) ")
    statement("if (!booleanIndex->toJvm) return -1")
    statement("booleanIndex->fromJvm = env->GetMethodID(booleanIndex->cls, \"booleanValue\",\"()Z\") ")
    statement("if (!booleanIndex->fromJvm) return -1")
}


fun Poet.mapBooleanFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJBoolean(JNIEnv *env, jobject jBoolean) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jBoolean ? std::make_shared<int>( int(env->CallBooleanMethod(jBoolean, booleanIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapBooleanToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJBoolean(JNIEnv *env, const std::shared_ptr<int>& cppBoolean) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return cppBoolean ? env->NewObject(booleanIndex->cls,booleanIndex->toJvm, jboolean( *cppBoolean ) ) : NULL")

    line("}")
}