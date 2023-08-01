package com.github.klee0kai.bridge.brooklyn.cpp

import com.github.klee0kai.bridge.brooklyn.BuildConfig
import com.github.klee0kai.bridge.brooklyn.poet.PoetDelegate

fun CodeBuilder.defHeaders() = apply {
    val unicHeaderName = file.nameWithoutExtension
    header
        .line("#ifndef $unicHeaderName")
        .line("#define $unicHeaderName")
        .include("brooklyn.h")
        .lines(2)

    footer
        .preLine("#endif //$unicHeaderName")
}


fun <T: PoetDelegate> T.include(lib: String) = apply {
    var formated = lib.trim()
    if (!formated.startsWith("\"") && !formated.startsWith("\"")) formated = "\"${formated}\""
    post("#include ${formated}\n")
}

fun <T: PoetDelegate> T.line(code: String) = apply { post("${code}\n") }
fun <T: PoetDelegate> T.preLine(code: String) = apply { pre("${code}\n") }

fun <T: PoetDelegate> T.lines(count: Int) = apply {
    repeat(count) { post("\n") }
}

fun <T: PoetDelegate> T.brooklynHeaderComment() = apply {
    post("// Generated code \n")
    post("// Brooklyn Bridge ${BuildConfig.KOTLIN_PLUGIN_VERSION} \n")
    post("// Project ${BuildConfig.KOTLIN_PLUGIN_SITE} \n")
    post("// Copyright (c) 2023 Andrey Kuzubov \n")
    lines(1)
}

fun <T: PoetDelegate> T.comment(body: String) = apply { post("// ${body}\n") }