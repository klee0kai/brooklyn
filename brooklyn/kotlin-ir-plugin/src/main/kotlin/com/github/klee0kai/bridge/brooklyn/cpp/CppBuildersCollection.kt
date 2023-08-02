package com.github.klee0kai.bridge.brooklyn.cpp

import java.io.File

class CppBuildersCollection(
    private val outDir: File
) {

    private val builders = HashMap<File, CodeBuilder>()

    fun getOrCreate(fileName: String): CodeBuilder {
        val file = File(outDir, fileName)
        builders.putIfAbsent(file, CodeBuilder(file))
        return builders[file]!!
    }

    fun getOrCreate(relativeFile: File): CodeBuilder {
        val file = File(outDir, relativeFile.path)
        builders.putIfAbsent(file, CodeBuilder(file))
        return builders[file]!!
    }


    fun genAll() {
        builders.forEach { (_, builder) -> builder.gen() }
    }

}
