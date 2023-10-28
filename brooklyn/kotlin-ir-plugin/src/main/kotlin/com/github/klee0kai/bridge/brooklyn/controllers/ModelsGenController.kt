package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.controllers.CommonGenController.Companion.headersInitBlock
import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.*
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.deInitBrooklyn
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.initBrooklyn
import com.github.klee0kai.bridge.brooklyn.cpp.model.declareClassModelStructure
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppMappingNameSpace
import com.github.klee0kai.bridge.brooklyn.di.DI
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.ir.util.classId

class ModelsGenController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val headerCreator by DI.kotlinVisitorLazy()

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        gen.getOrCreate(CommonNaming.modelHeader)
            .header {
                headerCreator.pojoJniClasses.forEach { include(it.classId!!.modelHeaderFile.path) }
                headerCreator.mirrorJniClasses.forEach { include(it.classId!!.modelHeaderFile.path) }
            }

        gen.getOrCreate(CommonNaming.modelCpp)
            .header {
                include(CommonNaming.modelHeader)
                include(CommonNaming.envHeader)
                include(CommonNaming.brooklynInternalHeader)
                statement("using namespace ${CommonNaming.BROOKLYN}")
            }
            .initBrooklyn(isImpl = true)
            .deInitBrooklyn(isImpl = true)


        headerCreator.pojoJniClasses.forEach { declaration ->
            launch {
                val clId = declaration.classId!!
                gen.getOrCreate(clId.mapperHeaderFile, headersInitBlock(namespaces = arrayOf(CommonNaming.MAPPER)))
                    .header {
                        include(CommonNaming.commonClassesMapperHeader)
                        include(declaration.classId!!.modelHeaderFile.path)
                    }
                    .namespaces(declaration.cppMappingNameSpace())
                    .declareClassIndexStructure(declaration, pojo = true)
                    .initJniClassApi()
                    .deinitJniClassApi()
                    .mapJniClass(declaration)

                gen.getOrCreate(
                    clId.mapperCppFile,
                    headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(CommonNaming.MAPPER))
                )
                    .header {
                        include(clId.mapperHeaderFile.path)
                        include(CommonNaming.mapperHeader)
                    }
                    .namespaces(declaration.cppMappingNameSpace())
                    .declareClassIndexField(declaration)
                    .initJniClassImpl(declaration, pojo = true)
                    .deinitJniClassImpl(declaration)
                    .mapJniClass(declaration, isImpl = true)

                gen.getOrCreate(clId.modelHeaderFile, headersInitBlock())
                    .declareClassModelStructure(declaration)
            }
        }
    }

}