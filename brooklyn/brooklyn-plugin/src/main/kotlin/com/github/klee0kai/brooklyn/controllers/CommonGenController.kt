package com.github.klee0kai.brooklyn.controllers

import com.github.klee0kai.brooklyn.di.DI
import com.github.klee0kai.brooklyn.cpp.common.*
import com.github.klee0kai.brooklyn.cpp.env.*
import kotlinx.coroutines.withContext

class CommonGenController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        gen.getOrCreate(relativeFile = CommonNaming.brooklynHeader)
            .allJniHeaders()
            .include(CommonNaming.mapperHeader)
            .include(CommonNaming.modelHeader)
            .include(CommonNaming.envHeader)

        gen.getOrCreate(relativeFile = CommonNaming.brooklynInternalHeader)
            .allJniHeaders()

        gen.getOrCreate(CommonNaming.envHeader, headersInitBlock())
            .initEnvJvm()
            .initEnv()
            .deInitEnv()
            .getEnv()
            .attachEnv()
            .detactEnv()
            .bindEnv()
    }


    companion object {
        fun headersInitBlock(
            doubleImportCheck: Boolean = true,
            vararg namespaces: String
        ): CodeBuilder.() -> Unit = {
            defHeaders(doubleImportCheck = doubleImportCheck)
            namespaces(CommonNaming.BROOKLYN, *namespaces)
        }
    }

}