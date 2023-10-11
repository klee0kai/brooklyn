package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement

fun CodeBuilder.mapFromJStringApi() = apply {
    body {
        statement("std::shared_ptr<std::string> mapJString(JNIEnv *env, jstring jString)")
    }
}

fun CodeBuilder.mapToJStringApi() = apply {
    body {
        statement("jstring mapToJString(JNIEnv *env, std::shared_ptr<std::string> str)")
    }
}


fun CodeBuilder.mapJStringImpl() = apply {
    body {
        line("std::shared_ptr<std::string> mapJString(JNIEnv *env, jstring jString){")
        statement("const char * tempChars = jString!= NULL ? env->GetStringUTFChars(jString, NULL) : NULL ")
        statement("std::shared_ptr<std::string> cStr = tempChars ? std::make_shared<std::string>( tempChars ) : std::shared_ptr<std::string>() ")
        statement("if ( tempChars ) env->ReleaseStringUTFChars(jString, tempChars)")
        statement("return cStr")
        line("}")
    }
}


fun CodeBuilder.mapToJStringImpl() = apply {
    body {
        line("jstring mapToJString(JNIEnv *env, std::shared_ptr<std::string> str){ ")
        statement("return str ? env->NewStringUTF( str->c_str() ) : NULL ")
        line("}")
    }
}