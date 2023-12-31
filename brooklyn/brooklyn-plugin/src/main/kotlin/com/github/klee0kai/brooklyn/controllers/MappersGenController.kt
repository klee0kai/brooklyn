package com.github.klee0kai.brooklyn.controllers

import com.github.klee0kai.brooklyn.controllers.CommonGenController.Companion.headersInitBlock
import com.github.klee0kai.brooklyn.cpp.common.CommonNaming
import com.github.klee0kai.brooklyn.cpp.common.include
import com.github.klee0kai.brooklyn.cpp.common.mapperHeaderFile
import com.github.klee0kai.brooklyn.cpp.mapper.deinitAllApi
import com.github.klee0kai.brooklyn.cpp.mapper.deinitAllImpl
import com.github.klee0kai.brooklyn.cpp.mapper.initAllApi
import com.github.klee0kai.brooklyn.cpp.mapper.initAllImpl
import com.github.klee0kai.brooklyn.cpp.mapper.std.deinitStdTypes
import com.github.klee0kai.brooklyn.cpp.mapper.std.initStdTypes
import com.github.klee0kai.brooklyn.cpp.mapper.std.stdTypeMappers
import com.github.klee0kai.brooklyn.di.DI
import kotlinx.coroutines.withContext

class MappersGenController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val booklynTypes by DI.brooklynTypes()

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        gen.getOrCreate(
            CommonNaming.commonClassesMapperHeader,
            headersInitBlock(namespaces = arrayOf(CommonNaming.MAPPER))
        )
            .initStdTypes()
            .deinitStdTypes()
            .stdTypeMappers()


        gen.getOrCreate(
            CommonNaming.commonClassesMapperCpp,
            headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(CommonNaming.MAPPER))
        )
            .initStdTypes(isImpl = true)
            .deinitStdTypes(isImpl = true)
            .stdTypeMappers(isImpl = true)


        gen.getOrCreate(CommonNaming.mapperHeader, headersInitBlock(namespaces = arrayOf(CommonNaming.MAPPER)))
            .header {
                include(CommonNaming.commonClassesMapperHeader)
                booklynTypes.pojoJniClasses.forEach { include(it.classId!!.mapperHeaderFile.path) }
            }
            .initAllApi()
            .deinitAllApi()


        gen.getOrCreate(
            CommonNaming.mapperCpp,
            headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(CommonNaming.MAPPER))
        )
            .include(CommonNaming.mapperHeader)
            .initAllImpl(booklynTypes.pojoJniClasses + booklynTypes.mirrorJniClasses)
            .deinitAllImpl(booklynTypes.pojoJniClasses + booklynTypes.mirrorJniClasses)


    }

}