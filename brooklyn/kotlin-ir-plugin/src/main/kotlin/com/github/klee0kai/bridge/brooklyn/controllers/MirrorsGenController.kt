package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.controllers.CommonGenController.Companion.headersInitBlock
import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.*
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.declareClassMirror
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.implMirrorInterface
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.implementClassMirror
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppMappingNameSpace
import com.github.klee0kai.bridge.brooklyn.di.DI
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.ir.util.classId

class MirrorsGenController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val headerCreator by DI.kotlinVisitorLazy()

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        headerCreator.mirrorJniClasses.forEach { declaration ->
            launch {
                val clId = declaration.classId!!
                gen.getOrCreate(clId.mapperHeaderFile, headersInitBlock(namespaces = arrayOf(CommonNaming.MAPPER)))
                    .header {
                        include(CommonNaming.commonClassesMapperHeader)
                        include(clId.modelHeaderFile.path)
                    }
                    .namespaces(declaration.cppMappingNameSpace())
                    .declareClassIndexStructure(declaration)
                    .initJniClassApi()
                    .deinitJniClassApi()
                    .mapMirrorClass(declaration)

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
                    .initJniClassImpl(declaration)
                    .deinitJniClassImpl(declaration)
                    .mapMirrorClass(declaration, isImpl = true)


                gen.getOrCreate(clId.modelHeaderFile, headersInitBlock())
                    .header { include(CommonNaming.envHeader) }
                    .declareClassMirror(declaration)


                gen.getOrCreate(clId.modelCppFile, headersInitBlock(doubleImportCheck = false))
                    .header {
                        include(clId.modelHeaderFile.path)
                        include(clId.mapperHeaderFile.path)
                        include(CommonNaming.envHeader)
                    }
                    .implementClassMirror(declaration)

                gen.getOrCreate(clId.interfaceCppFile)
                    .header {
                        include(clId.modelHeaderFile.path)
                        include(clId.mapperHeaderFile.path)
                        include(CommonNaming.envHeader)
                        statement("using namespace ${CommonNaming.BROOKLYN}")
                    }
                    .implMirrorInterface(declaration)
            }
        }
    }

}