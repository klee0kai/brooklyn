package com.github.klee0kai.brooklyn.controllers

import com.github.klee0kai.brooklyn.controllers.CommonGenController.Companion.headersInitBlock
import com.github.klee0kai.brooklyn.cpp.mirror.declareClassMirror
import com.github.klee0kai.brooklyn.cpp.mirror.implMirrorInterface
import com.github.klee0kai.brooklyn.cpp.mirror.implementClassMirror
import com.github.klee0kai.brooklyn.cpp.typemirros.cppMappingNameSpace
import com.github.klee0kai.brooklyn.di.DI
import com.github.klee0kai.brooklyn.cpp.common.*
import com.github.klee0kai.brooklyn.cpp.mapper.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.file

class MirrorsGenController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val brooklynTypes by DI.brooklynTypes()

    private val gen by DI.cppBuilderLazy()

    suspend fun gen() = withContext(defDispatcher) {
        brooklynTypes.nonCachedMirrorJniClasses.forEach { declaration ->
            val srcFile = declaration.file.path
            launch {
                val clId = declaration.classId!!
                gen.getOrCreate(
                    clId.mapperHeaderFile,
                    srcFile = srcFile,
                    initBlock = headersInitBlock(namespaces = arrayOf(CommonNaming.MAPPER))
                )
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
                    srcFile = srcFile,
                    initBlock = headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(CommonNaming.MAPPER))
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


                gen.getOrCreate(
                    clId.modelHeaderFile,
                    srcFile = srcFile,
                    initBlock = headersInitBlock()
                )
                    .header { include(CommonNaming.envHeader) }
                    .declareClassMirror(declaration)

                gen.getOrCreate(
                    clId.modelCppFile,
                    srcFile = srcFile,
                    initBlock = headersInitBlock(doubleImportCheck = false)
                )
                    .header {
                        include(clId.modelHeaderFile.path)
                        include(clId.mapperHeaderFile.path)
                        include(CommonNaming.envHeader)
                    }
                    .implementClassMirror(declaration)

                gen.getOrCreate(
                    clId.interfaceCppFile,
                    srcFile = srcFile,
                )
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