package com.github.klee0kai.bridge.brooklyn.cpp.common

import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrDeclarationWithName
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.isVararg
import org.jetbrains.kotlin.name.ClassId
import java.io.File
import kotlin.math.absoluteValue

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
    const val modelCpp = "model/model.cpp"

    const val envHeader = "env/env.h"
    const val envCpp = "env/env.cpp"

    const val findBrooklynCmake = "FindBrooklynBridge.cmake"
}

val ClassId.mapperHeaderFile
    get() = File("mappers", "${cppFilePrefix}_mapper.h")

val ClassId.mapperCppFile
    get() = File("mappers", "${cppFilePrefix}_mapper.cpp")

val ClassId.modelHeaderFile
    get() = File("model", "${cppFilePrefix}.h")
val ClassId.modelCppFile
    get() = File("model", "${cppFilePrefix}.cpp")

val ClassId.interfaceCppFile
    get() = File("mirror", "${cppFilePrefix}_interface.cpp")


val ClassId.indexStructName
    get() = "${croppedFullName}IndexStructure".camelCase().firstUppercase()

val ClassId.indexVariableName
    get() = "${croppedFullName}Index".camelCase()

val IrFunction.cppNameMirror
    get() = "$name${
        fullValueParameterList.map { it.type.classFqName to it.isVararg }.hashCode().absoluteValue
    }".camelCase()


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


fun String.firstUppercase() = first().uppercaseChar() + substring(1)


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
    get() = "${croppedPackageName.snakeCase()}_${shortClassName.toString().snakeCase()}"

val IrDeclarationWithName.nameUpperCase get() = name.toString().firstUppercase()


private fun String.trimSpecSymbols() =
    this.replace(".", "_").filter { it !in "<>" }