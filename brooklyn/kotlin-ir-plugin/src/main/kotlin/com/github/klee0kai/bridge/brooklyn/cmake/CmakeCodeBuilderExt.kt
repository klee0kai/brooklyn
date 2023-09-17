package com.github.klee0kai.bridge.brooklyn.cmake

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines

fun CodeBuilder.cmakeLib(
    libName: String,
    rootDir: String,
    src: List<String>,
) = apply {
    line("find_package(JNI REQUIRED)")
    line("if (JNI_FOUND)")
    line("    message(STATUS \"JNI_INCLUDE_DIRS=\${JNI_INCLUDE_DIRS}\")")
    line("    message(STATUS \"JNI_LIBRARIES=\${JNI_LIBRARIES}\")")
    line("endif ()")


    line("add_library($libName")
    src.forEach {
        post("\t")
        post(it)
        lines(1)
    }
    line(")")

    line("target_link_libraries(${libName} \${JNI_LIBRARIES})")

    lines(1)
    line("set_target_properties(${libName} PROPERTIES INTERFACE_INCLUDE_DIRECTORIES $rootDir)")
    line("set_target_properties(${libName} PROPERTIES POSITION_INDEPENDENT_CODE ON)")

    line("target_include_directories(${libName} PUBLIC  \${JNI_INCLUDE_DIRS}\n\t $rootDir )")
}