package com.github.klee0kai.bridge.brooklyn.cpp

import java.io.File

class CppBuildersCollection(
    private val outDir: File
) {

    private val builders = HashMap<File, CodeBuilder>()

    fun getOrCreate(pkg: String, clName: String, create: (CodeBuilder) -> Unit): CodeBuilder {
        val name = "${pkg.snakeCase()}_${clName.snakeCase()}.h"
        val file = File(outDir, name)

        if (!builders.contains(file)) {
            builders.putIfAbsent(file, CodeBuilder(file).apply(create))
        }
        return builders[file]!!
    }

    fun getOrCreate(fileName: String): CodeBuilder {
        val file = File(outDir, fileName)
        builders.putIfAbsent(file, CodeBuilder(file))
        return builders[file]!!
    }

    fun genAll() {
        builders.forEach { (_, builder) -> builder.gen() }
    }

}

val CppBuildersCollection.jmappersHeader get() = getOrCreate("jmappers.h")
val CppBuildersCollection.jmappersCpp get() = getOrCreate("jmappers.cpp")