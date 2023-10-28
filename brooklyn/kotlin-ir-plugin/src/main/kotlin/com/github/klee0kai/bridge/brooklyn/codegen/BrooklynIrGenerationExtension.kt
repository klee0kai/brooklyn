package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.cache.CacheStore
import com.github.klee0kai.bridge.brooklyn.cache.minus
import com.github.klee0kai.bridge.brooklyn.cmake.cmakeLib
import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.BROOKLYN
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.MAPPER
import com.github.klee0kai.bridge.brooklyn.cpp.env.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.std.deinitStdTypes
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.std.initStdTypes
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.std.stdTypeMappers
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.*
import com.github.klee0kai.bridge.brooklyn.cpp.model.declareClassModelStructure
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedMirrorClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedPojoClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.allCppTypeMirrors
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppMappingNameSpace
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import java.io.File
import java.lang.ref.WeakReference

class BrooklynIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val outDirFile: String,
    private val cachePath: String,
) : IrGenerationExtension {

    private fun headersInitBlock(
        doubleImportCheck: Boolean = true,
        vararg namespaces: String
    ): CodeBuilder.() -> Unit = {
        defHeaders(doubleImportCheck = doubleImportCheck)
        namespaces(BROOKLYN, *namespaces)
    }


    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext): Unit = runBlocking {
        pluginContextRef = WeakReference(pluginContext)
        File(outDirFile).deleteRecursively()
        val cacheStore = CacheStore(cachePath)
        val oldFingerPrint = cacheStore.loadAndResetProjectFingerPrint()
        val newFingerPrint = cacheStore.calcProjectFingerPrint(moduleFragment.files.map { File(it.path) })
        val diff = newFingerPrint - oldFingerPrint

        val headerCreator = KotlinVisitor()
        moduleFragment.files.forEach { file ->
            file.acceptVoid(headerCreator)
        }


        headerCreator.pojoJniClasses.forEach {
            allCppTypeMirrors.addSupportedPojoClass(it)
        }
        headerCreator.mirrorJniClasses.forEach {
            allCppTypeMirrors.addSupportedMirrorClass(it)
        }


        val gen = CppBuildersCollection(File(outDirFile))
        gen.getOrCreate(fileName = CommonNaming.brooklynHeader)
            .allJniHeaders()
            .include(CommonNaming.mapperHeader)
            .include(CommonNaming.modelHeader)
            .include(CommonNaming.envHeader)

        gen.getOrCreate(fileName = CommonNaming.brooklynInternalHeader)
            .allJniHeaders()

        gen.getOrCreate(CommonNaming.envHeader, headersInitBlock())
            .initEnvJvm()
            .initEnv()
            .deInitEnv()
            .getEnv()
            .attachEnv()
            .detactEnv()
            .bindEnv()

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


        gen.getOrCreate(CommonNaming.commonClassesMapperHeader, headersInitBlock(namespaces = arrayOf(MAPPER)))
            .initStdTypes()
            .deinitStdTypes()
            .stdTypeMappers()


        gen.getOrCreate(
            CommonNaming.commonClassesMapperCpp,
            headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
        )
            .initStdTypes(isImpl = true)
            .deinitStdTypes(isImpl = true)
            .stdTypeMappers(isImpl = true)

        headerCreator.pojoJniClasses.forEach { declaration ->
            val clId = declaration.classId!!
            gen.getOrCreate(clId.mapperHeaderFile, headersInitBlock(namespaces = arrayOf(MAPPER)))
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
                headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
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

        headerCreator.mirrorJniClasses.forEach { declaration ->
            val clId = declaration.classId!!
            gen.getOrCreate(clId.mapperHeaderFile, headersInitBlock(namespaces = arrayOf(MAPPER)))
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
                headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
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
                    statement("using namespace $BROOKLYN")
                }
                .implMirrorInterface(declaration)


        }

        gen.getOrCreate(CommonNaming.mapperHeader, headersInitBlock(namespaces = arrayOf(MAPPER)))
            .header {
                include(CommonNaming.commonClassesMapperHeader)
                headerCreator.pojoJniClasses.forEach { include(it.classId!!.mapperHeaderFile.path) }
            }
            .initAllApi()
            .deinitAllApi()


        gen.getOrCreate(
            CommonNaming.mapperCpp,
            headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
        )
            .initAllImpl(headerCreator.pojoJniClasses + headerCreator.mirrorJniClasses)
            .deinitAllImpl(headerCreator.pojoJniClasses + headerCreator.mirrorJniClasses)


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
                statement("using namespace $BROOKLYN")
            }
            .initBrooklyn(isImpl = true)
            .deInitBrooklyn(isImpl = true)

        gen.genAll()


        CodeBuilder(File(outDirFile, CommonNaming.findBrooklynCmake))
            .cmakeLib(
                libName = BROOKLYN,
                rootDir = outDirFile,
                src = gen.files
                    .filter { it.extension.endsWith("cpp") }
                    .map { it.absolutePath }
            )
            .gen(sym = "#")


        cacheStore.save(newFingerPrint)
    }

    companion object {
        var pluginContextRef: WeakReference<IrPluginContext>? = null
    }


}