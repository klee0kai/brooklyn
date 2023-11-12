package com.github.klee0kai.brooklyn.cpp.mirror

import com.github.klee0kai.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.brooklyn.cpp.common.line
import com.github.klee0kai.brooklyn.cpp.common.statement

fun CodeBuilder.initBrooklyn(isImpl: Boolean = false) = apply {
    line("extern \"C\" JNIEXPORT jint JNICALL")
    line("Java_com_github_klee0kai_brooklyn_Brooklyn_initLib(")
    line("        JNIEnv *env,")
    line("        jobject /* this */ ) ")
    if (!isImpl){
        statement("")
        return@apply
    }
    line("{")
    statement("    int initResult = brooklyn::init(env)")
    statement("    return initResult")
    line("}")

}

fun CodeBuilder.deInitBrooklyn(isImpl: Boolean = false) = apply {
    line("extern \"C\" JNIEXPORT jint JNICALL")
    line("Java_com_github_klee0kai_brooklyn_Brooklyn_deInitLib(")
    line(" JNIEnv *env,")
    line(" jobject /* this */ ) ")
    if (!isImpl){
        statement("")
        return@apply
    }
    line("{")
    statement("int initResult = brooklyn::init(env)")
    statement("return initResult")
    line("}")
}
