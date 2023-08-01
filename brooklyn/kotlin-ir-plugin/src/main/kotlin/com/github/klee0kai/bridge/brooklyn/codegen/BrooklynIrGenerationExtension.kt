package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.cpp.CppBuildersCollection
import com.github.klee0kai.bridge.brooklyn.cpp.allJniHeaders
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import java.io.File

class BrooklynIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val outDirFile: String
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val buildCollections = CppBuildersCollection(File(outDirFile))
        buildCollections.createCommonHeaders()

        moduleFragment.files.forEach { file ->
            val headerCreator = KotlinVisitor(gen = buildCollections)
            file.acceptVoid(headerCreator)
        }
        buildCollections.genAll()
    }

    private fun CppBuildersCollection.createCommonHeaders() {
        getOrCreate(fileName = "brooklyn.h")
            .allJniHeaders()
    }

}