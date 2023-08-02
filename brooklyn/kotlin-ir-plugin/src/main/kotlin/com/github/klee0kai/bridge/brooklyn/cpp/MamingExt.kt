package com.github.klee0kai.bridge.brooklyn.cpp

import org.jetbrains.kotlin.name.ClassId
import java.io.File


val ClassId.mapperHeaderFile
    get() = File("mappers", "${cppFilePrefix}_mapper.h")

val ClassId.mapperCppFile
    get() = File("mappers", "${cppFilePrefix}_mapper.cpp")

val ClassId.structuresHeaderFile
    get() = File("${cppFilePrefix}.header")

fun String.camelCase() = buildString {
    var specSymbol = false
    trimSpecSymbols().forEach {
        when {
            it in "._" -> specSymbol = true
            specSymbol -> {
                append(it.uppercaseChar())
                specSymbol = false
            }

            else -> append(it)
        }
    }
}


fun String.firstCamelCase() = first().uppercaseChar() + substring(1)


fun String.snakeCase(sep: String = "_") = buildString {
    var specSymbol = false
    trimSpecSymbols().forEach {
        when {
            it in "._/" -> specSymbol = true
            specSymbol -> {
                append(sep)
                append(it)
                specSymbol = false
            }

            else -> {
                append(it)
            }
        }
    }
}

private val ClassId.cppFilePrefix
    get() = "${packageFqName.toString().snakeCase()}_${shortClassName.toString().snakeCase()}"

private fun String.trimSpecSymbols() =
    this.replace(".", "_").filter { it !in "<>" }