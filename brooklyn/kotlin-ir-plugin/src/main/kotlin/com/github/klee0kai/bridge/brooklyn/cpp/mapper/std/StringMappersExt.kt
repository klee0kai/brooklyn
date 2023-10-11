package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement

fun CodeBuilder.mapFromJString(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<std::string> mapJString(JNIEnv *env, jstring jString) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare {")
        statement("const char * tempChars = jString!= NULL ? env->GetStringUTFChars(jString, NULL) : NULL ")
        statement("std::shared_ptr<std::string> cStr = tempChars ? std::make_shared<std::string>( tempChars ) : std::shared_ptr<std::string>() ")
        statement("if ( tempChars ) env->ReleaseStringUTFChars(jString, tempChars)")
        statement("return cStr")
        line("}")
    }
}


fun CodeBuilder.mapToJString(isImpl: Boolean = false) = apply {
    val declare = "jstring mapToJString(JNIEnv *env, const std::shared_ptr<std::string>& str) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare { ")
        statement("return str ? env->NewStringUTF( str->c_str() ) : NULL ")
        line("}")
    }
}