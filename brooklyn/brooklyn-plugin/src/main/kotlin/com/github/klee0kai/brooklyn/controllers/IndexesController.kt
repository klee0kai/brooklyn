package com.github.klee0kai.brooklyn.controllers

import com.github.klee0kai.brooklyn.controllers.CommonGenController.Companion.headersInitBlock
import com.github.klee0kai.brooklyn.cpp.common.CommonNaming
import com.github.klee0kai.brooklyn.cpp.common.include
import com.github.klee0kai.brooklyn.di.DI
import com.github.klee0kai.brooklyn.cpp.env.*
import kotlinx.coroutines.withContext

class IndexesController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        gen.getOrCreate(CommonNaming.envCpp, headersInitBlock(doubleImportCheck = false))
            .header {
                include("<thread>")
                include(CommonNaming.envHeader)
                include(CommonNaming.mapperHeader)
            }
            .envCppVariables()
            .initEnvJvm(isImpl = true)
            .initEnv(isImpl = true)
            .deInitEnv(isImpl = true)
            .getEnv(isImpl = true)
            .attachEnv(isImpl = true)
            .detactEnv(isImpl = true)
            .bindEnv(isImpl = true)
    }


}