package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.cmake.cmakeLib
import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming
import com.github.klee0kai.bridge.brooklyn.di.DI
import kotlinx.coroutines.withContext
import java.io.File

class CmakeGenController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val config by lazy { DI.config() }

    suspend fun gen() = withContext(defDispatcher) {
       val cppFiles =  File(config.outDirFile).walkTopDown()
            .filter { it.extension.endsWith("cpp") }

        CodeBuilder(File(config.outDirFile, CommonNaming.findBrooklynCmake))
            .cmakeLib(
                libName = CommonNaming.BROOKLYN,
                rootDir = config.outDirFile,
                src = cppFiles
                    .map { it.absolutePath }
                    .toList()
            )
            .gen(sym = "#")
    }


}