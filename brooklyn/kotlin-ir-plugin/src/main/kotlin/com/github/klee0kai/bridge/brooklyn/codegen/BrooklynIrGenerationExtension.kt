package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.cmake.cmakeLib
import com.github.klee0kai.bridge.brooklyn.cpp.*
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import java.io.File

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
        val gen = CppBuildersCollection(File(outDirFile))
        gen.createCommonHeaders()

        val headerCreator = KotlinVisitor()
        moduleFragment.files.forEach { it.acceptVoid(headerCreator) }

        headerCreator.pojoJniClasses.forEach { declaration ->
            val clId = declaration.classId!!
            gen.getOrCreate(clId.mapperHeaderFile, headerInitBlock)
                .initJniClassApi(declaration)
                .deinitJniClassApi(declaration)


            gen.getOrCreate(clId.mapperCppFile, cppInitBlock)
                .header { include(clId.mapperHeaderFile.path) }
                .declareClassIndexStructure(declaration)
                .initJniClassImpl(declaration)
                .deinitJniClassImpl(declaration)
        }

        gen.getOrCreate("mappers/mapper.h", headerInitBlock)
            .initAllApi()
            .initAllFromJvmApi()
            .deinitAllApi()


        gen.getOrCreate("mappers/mapper.cpp", cppInitBlock)
            .initAllImpl(headerCreator.pojoJniClasses.mapNotNull { it.classId })
            .initAllFromJvmImpl()
            .deinitAllImpl(headerCreator.pojoJniClasses.mapNotNull { it.classId })


        gen.genAll()


        CodeBuilder(File(outDirFile, "FindBrooklynBridge.cmake"))
            .cmakeLib(
                libName = "brooklyn",
                rootDir = outDirFile,
                src = gen.files
                    .filter { it.extension.endsWith("cpp") }
                    .map { it.absolutePath }
            )
            .gen(sym = "#")

    }

    private fun CppBuildersCollection.createCommonHeaders() {
        getOrCreate(fileName = "brooklyn.h")
            .allJniHeaders()
    }

}