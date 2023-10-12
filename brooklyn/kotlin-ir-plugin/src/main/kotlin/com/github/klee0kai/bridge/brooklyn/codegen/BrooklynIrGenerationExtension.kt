package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.cmake.cmakeLib
import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.BROOKLYN
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.MAPPER
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.std.deinitStdTypes
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.std.initStdTypes
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.std.stdTypeMappers
import com.github.klee0kai.bridge.brooklyn.cpp.mirror.declareClassMirror
import com.github.klee0kai.bridge.brooklyn.cpp.model.declareClassModelStructure
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedPojoClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.allCppTypeMirrors
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppMappingNameSpace
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import java.io.File
import java.lang.ref.WeakReference

class BrooklynIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val outDirFile: String
) : IrGenerationExtension {

    private fun headersInitBlock(
        doubleImportCheck: Boolean = true,
        vararg namespaces: String
    ): CodeBuilder.() -> Unit = {
        defHeaders(doubleImportCheck = doubleImportCheck)
        namespaces(BROOKLYN, *namespaces)
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        pluginContextRef = WeakReference(pluginContext)
        File(outDirFile).deleteRecursively()

        val headerCreator = KotlinVisitor()
        moduleFragment.files.forEach { it.acceptVoid(headerCreator) }


        headerCreator.pojoJniClasses.forEach {
            allCppTypeMirrors.addSupportedPojoClass(it)
        }


        val gen = CppBuildersCollection(File(outDirFile))
        gen.getOrCreate(fileName = CommonNaming.brooklynHeader)
            .allJniHeaders()
            .include(CommonNaming.mapperHeader)
            .include(CommonNaming.modelHeader)

        gen.getOrCreate(fileName = CommonNaming.brooklynInternalHeader)
            .allJniHeaders()

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
                .declareClassIndexStructure(declaration)
                .initJniClassApi()
                .deinitJniClassApi()
                .mapJniClassApi(declaration)

            gen.getOrCreate(
                clId.mapperCppFile,
                headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
            )
                .header { include(clId.mapperHeaderFile.path) }
                .header { include(CommonNaming.mapperHeader) }
                .namespaces(declaration.cppMappingNameSpace())
                .declareClassIndexField(declaration)
                .initJniClassImpl(declaration)
                .deinitJniClassImpl(declaration)
                .mapJniClassImpl(declaration)

            gen.getOrCreate(clId.modelHeaderFile, headersInitBlock())
                .declareClassModelStructure(declaration)
        }

        headerCreator.mirrorJniClasses.forEach { declaration ->
            val clId = declaration.classId!!
            gen.getOrCreate(clId.mapperHeaderFile, headersInitBlock(namespaces = arrayOf(MAPPER)))
                .header {
                    include(CommonNaming.commonClassesMapperHeader)
                }
                .namespaces(declaration.cppMappingNameSpace())
                .declareClassIndexStructure(declaration)
                .initJniClassApi()
                .deinitJniClassApi()



            gen.getOrCreate(
                clId.mapperCppFile,
                headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
            )
                .header { include(clId.mapperHeaderFile.path) }
                .header { include(CommonNaming.mapperHeader) }
                .namespaces(declaration.cppMappingNameSpace())
                .declareClassIndexField(declaration)
                .initJniClassImpl(declaration)
                .deinitJniClassImpl(declaration)

            gen.getOrCreate(clId.mirrorHeaderFile, headersInitBlock())
                .declareClassMirror(declaration)

        }

        gen.getOrCreate(CommonNaming.mapperHeader, headersInitBlock(namespaces = arrayOf(MAPPER)))
            .header {
                include(CommonNaming.commonClassesMapperHeader)
                headerCreator.pojoJniClasses.forEach { include(it.classId!!.mapperHeaderFile.path) }
            }
            .initAllApi()
            .initAllFromJvmApi()
            .deinitAllApi()


        gen.getOrCreate(
            CommonNaming.mapperCpp,
            headersInitBlock(doubleImportCheck = false, namespaces = arrayOf(MAPPER))
        )
            .initAllImpl(headerCreator.pojoJniClasses + headerCreator.mirrorJniClasses)
            .initAllFromJvmImpl()
            .deinitAllImpl(headerCreator.pojoJniClasses + headerCreator.mirrorJniClasses)


        gen.getOrCreate(CommonNaming.modelHeader)
            .header { headerCreator.pojoJniClasses.forEach { include(it.classId!!.modelHeaderFile.path) } }

        gen.getOrCreate(CommonNaming.mirrorHeader)
            .header { headerCreator.mirrorJniClasses.forEach { include(it.classId!!.mirrorHeaderFile.path) } }

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

    }

    companion object {
        var pluginContextRef: WeakReference<IrPluginContext>? = null
    }


}