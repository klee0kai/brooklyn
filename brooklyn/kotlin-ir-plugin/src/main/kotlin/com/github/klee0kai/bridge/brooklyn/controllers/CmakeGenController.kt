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

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        CodeBuilder(File(config.outDirFile, CommonNaming.findBrooklynCmake))
            .cmakeLib(
                libName = CommonNaming.BROOKLYN,
                rootDir = config.outDirFile,
                src = gen.files
                    .filter { it.extension.endsWith("cpp") }
                    .map { it.absolutePath }
            )
            .gen(sym = "#")

    }

}