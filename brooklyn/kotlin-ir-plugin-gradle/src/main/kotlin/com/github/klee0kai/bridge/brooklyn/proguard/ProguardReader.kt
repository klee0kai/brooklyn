package com.github.klee0kai.bridge.brooklyn.proguard

import java.io.File
import kotlin.math.absoluteValue

object ProguardReader {

    fun readFromMapFile(file: File): List<ProguardClassMapping> {
        val classes = mutableListOf<ProguardClassMapping>()
        var curClass: ProguardClassMapping? = null
        file.readLines().forEach {
            var line = it
            if (line.endsWith(":")) line = line.substring(0, line.lastIndex)

            val split = line.split(*" -<>(),".toCharArray())
                .filter { it.isNotBlank() }

            when {
                line.startsWith("    ") && line.contains("(") && line.contains(")") -> {
                    val funcName = split[1]
                    val args = split.subList(2, split.lastIndex)
                    val cppNameMirror = "${funcName}${args.hashCode().absoluteValue}"
                    println("func ${curClass?.fullNameOriginal}#${cppNameMirror}")
                }

                line.startsWith("    ") -> {
                    curClass?.methods?.add(
                        ProguardMapping(
                            fullNameOriginal = split[1],
                            obfuscatedName = split.last(),
                        )
                    )
                }

                else -> {
                    if (curClass != null) classes.add(curClass!!)
                    curClass = ProguardClassMapping(
                        fullNameOriginal = split[0],
                        obfuscatedName = split.last()
                    )
                }
            }
        }

        if (curClass != null) classes.add(curClass!!)
        return classes
    }

}