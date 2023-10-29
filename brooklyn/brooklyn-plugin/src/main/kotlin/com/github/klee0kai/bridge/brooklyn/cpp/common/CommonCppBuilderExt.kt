package com.github.klee0kai.bridge.brooklyn.cpp.common

import com.github.klee0kai.bridge.brooklyn.BuildConfig
import com.github.klee0kai.bridge.brooklyn.poet.PoetDelegate

fun CodeBuilder.defHeaders(doubleImportCheck: Boolean = false) = apply {
    val unicHeaderName = "BROOKLYN_${file.nameWithoutExtension}"
    header {
        if (doubleImportCheck) {
            line("#ifndef $unicHeaderName")
            line("#define $unicHeaderName")
        }

        include(CommonNaming.brooklynInternalHeader)
        lines(2)
    }

    footer {
        if (doubleImportCheck) preLine("#endif //$unicHeaderName")
    }
}

fun CodeBuilder.namespaces(vararg namespaces: String) = apply {
    namespaces.filter { it.isNotEmpty() }.forEach { namespace ->
        variables {
            line("namespace $namespace {")
        }
        footer {
            preLine("} // namespace $namespace")
        }
    }
}

fun <T : PoetDelegate> T.include(lib: String) = apply {
    var formated = lib.trim()
    if (!formated.startsWith("\"") && !formated.startsWith("<")) formated = "\"${formated}\""
    post("#include ${formated}\n")
}

fun <T : PoetDelegate> T.line(code: String) = apply { post("${code}\n") }

fun <T : PoetDelegate> T.statement(code: String) = apply { post("${code};\n") }

fun <T : PoetDelegate> T.str(code: String) = apply { post("\"${code}\"") }

fun <T : PoetDelegate> T.preLine(code: String) = apply { pre("${code}\n") }

fun <T : PoetDelegate> T.lines(count: Int) = apply {
    repeat(count) { post("\n") }
}

fun <T : PoetDelegate> T.brooklynHeaderComment(sym: String = "//") = apply {
    post("$sym Generated code \n")
    post("$sym Brooklyn Bridge ${BuildConfig.KOTLIN_PLUGIN_VERSION} \n")
    post("$sym Project ${BuildConfig.KOTLIN_PLUGIN_SITE} \n")
    post("$sym Copyright (c) 2023 Andrey Kuzubov \n")
    lines(1)
}


fun <T : PoetDelegate> T.comment(body: String) = apply { post("// ${body}\n") }