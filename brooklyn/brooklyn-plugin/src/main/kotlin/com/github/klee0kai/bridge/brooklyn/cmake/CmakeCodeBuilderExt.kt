package com.github.klee0kai.bridge.brooklyn.cmake

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines

fun CodeBuilder.cmakeLib(
    libName: String,
    rootDir: String,
    src: List<String>,
) = apply {

    line("set(BROOKLYN_SRC ")
    src.forEach { line("\t \"$it\"") }
    line(")")
    line("set(BROOKLYN_INCLUDE_DIRS \"$rootDir\" )")

    lines(1)
}