package com.github.klee0kai.bridge.brooklyn.cpp

import java.io.File

class CppBuildersCollection(
    private val outDir: File
) {

    private val builders = HashMap<File, CodeBuilder>()

    fun getOrCreate(fileName: String, initBlock: CodeBuilder.() -> Unit = {}): CodeBuilder {
        val file = File(outDir, fileName)
        if (!builders.contains(file)) builders.putIfAbsent(file, CodeBuilder(file).apply(initBlock))
        return builders[file]!!
    }

    fun getOrCreate(relativeFile: File, initBlock: CodeBuilder.() -> Unit = {}): CodeBuilder {
        val file = File(outDir, relativeFile.path)
        if (!builders.contains(file)) builders.putIfAbsent(file, CodeBuilder(file).apply(initBlock))
        return builders[file]!!
    }


    fun genAll() {
        builders.forEach { (_, builder) -> builder.gen() }
    }

}
