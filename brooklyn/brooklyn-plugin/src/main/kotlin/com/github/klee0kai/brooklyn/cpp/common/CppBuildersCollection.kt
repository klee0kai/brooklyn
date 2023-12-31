package com.github.klee0kai.brooklyn.cpp.common

import com.github.klee0kai.brooklyn.di.DI
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CppBuildersCollection(
    private val outDir: File
) {

    private val ioDispatcher = DI.dispatchersModule().ioDispatcher()

    private val builders = HashMap<File, CodeBuilder>()

    val files get() = builders.keys

    @Synchronized
    fun getOrCreate(
        relativeFile: String,
        initBlock: CodeBuilder.() -> Unit = {}
    ): CodeBuilder = getOrCreate(File(relativeFile), srcFile = null, initBlock = initBlock)

    @Synchronized
    fun getOrCreate(
        relativeFile: File,
        srcFile: String? = null,
        initBlock: CodeBuilder.() -> Unit = {}
    ): CodeBuilder {
        val file = File(outDir, relativeFile.path)
        if (!builders.contains(file)) builders.putIfAbsent(file, CodeBuilder(file).apply(initBlock))
        return builders[file]!!
    }

    suspend fun genAll() = withContext(ioDispatcher) {
        builders.forEach { (_, builder) ->
            launch { builder.gen() }
        }
    }

}
