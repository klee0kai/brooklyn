package com.github.klee0kai.bridge.brooklyn

import com.github.klee0kai.bridge.brooklyn.codgen.FileBuildersCollection
import com.github.klee0kai.bridge.brooklyn.codgen.KotlinVisitor
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
        val buildCollections = FileBuildersCollection(File(outDirFile))
        moduleFragment.files.forEach { file ->
            val headerCreator = KotlinVisitor(buildCollections)
            file.acceptVoid(headerCreator)
        }
        buildCollections.genAll()
    }

}