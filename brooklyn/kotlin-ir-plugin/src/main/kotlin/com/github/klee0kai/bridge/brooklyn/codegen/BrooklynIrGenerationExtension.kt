package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.cmake.cmakeLib
import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.*
import com.github.klee0kai.bridge.brooklyn.cpp.model.declareClassModelStructure
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedPojoClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.allCppTypeMirrors
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

    private val headerInitBlock: CodeBuilder.() -> Unit = {
        defHeaders(doubleImportCheck = true)
        namespaces("brooklyn", "mapper")
    }

    private val cppInitBlock: CodeBuilder.() -> Unit = {
        defHeaders()
        namespaces("brooklyn", "mapper")
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

        headerCreator.pojoJniClasses.forEach { declaration ->
            val clId = declaration.classId!!
            gen.getOrCreate(clId.mapperHeaderFile, headerInitBlock)
                .header { include(declaration.classId!!.modelHeaderFile.path) }
                .initJniClassApi(declaration)
                .deinitJniClassApi(declaration)
                .mapJniClassApi(declaration)

            gen.getOrCreate(clId.mapperCppFile, cppInitBlock)
                .header { include(clId.mapperHeaderFile.path) }
                .declareClassIndexStructure(declaration)
                .initJniClassImpl(declaration)
                .deinitJniClassImpl(declaration)
                .mapJniClassImpl(declaration)

            gen.getOrCreate(clId.modelHeaderFile, headerInitBlock)
                .declareClassModelStructure(declaration)


        }

        gen.getOrCreate(CommonNaming.mapperHeader, headerInitBlock)
            .initAllApi()
            .initAllFromJvmApi()
            .deinitAllApi()


        gen.getOrCreate(CommonNaming.mapperCpp, cppInitBlock)
            .initAllImpl(headerCreator.pojoJniClasses.mapNotNull { it.classId })
            .initAllFromJvmImpl()
            .deinitAllImpl(headerCreator.pojoJniClasses.mapNotNull { it.classId })


        gen.getOrCreate(CommonNaming.modelHeader)
            .apply { headerCreator.pojoJniClasses.forEach { include(it.classId!!.modelHeaderFile.path) } }

        gen.genAll()


        CodeBuilder(File(outDirFile, CommonNaming.findBrooklynCmake))
            .cmakeLib(
                libName = "brooklyn",
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