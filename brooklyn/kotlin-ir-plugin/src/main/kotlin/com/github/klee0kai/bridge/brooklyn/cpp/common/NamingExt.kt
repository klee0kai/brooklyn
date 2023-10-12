package com.github.klee0kai.bridge.brooklyn.cpp.common

import org.jetbrains.kotlin.name.ClassId
import java.io.File

object CommonNaming {
    const val BROOKLYN = "brooklyn"
    const val MAPPER = "mapper"

    const val brooklynHeader = "brooklyn.h"
    const val brooklynInternalHeader = "lib/brooklyn_internal.h"

    const val mapperHeader = "mappers/mapper.h"
    const val mapperCpp = "mappers/mapper.cpp"

    const val commonClassesMapperHeader = "mappers/common_mapper.h"
    const val commonClassesMapperCpp = "mappers/common_mapper.cpp"

    const val modelHeader = "model/model.h"
    const val mirrorHeader = "mirror/mirror.h"

    const val findBrooklynCmake = "FindBrooklynBridge.cmake"
}

val ClassId.mapperHeaderFile
    get() = File("mappers", "${cppFilePrefix}_mapper.h")

val ClassId.mapperCppFile
    get() = File("mappers", "${cppFilePrefix}_mapper.cpp")

val ClassId.modelHeaderFile
    get() = File("model", "${cppFilePrefix}_model.h")

val ClassId.mirrorHeaderFile
    get() = File("mirror", "${cppFilePrefix}_mirror.h")
val ClassId.mirrorCppFile
    get() = File("mirror", "${cppFilePrefix}_mirror.cpp")
val ClassId.interfaceCppFile
    get() = File("mirror", "${cppFilePrefix}_interface.cpp")


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