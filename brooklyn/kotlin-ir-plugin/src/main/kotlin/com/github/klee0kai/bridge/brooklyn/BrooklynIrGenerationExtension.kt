package com.github.klee0kai.bridge.brooklyn

import com.github.klee0kai.bridge.brooklyn.codgen.CppHeaderCreater
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.incremental.mkdirsOrThrow
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptVoid
import java.io.File

class BrooklynIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val outDirFile: String
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        File(outDirFile).mkdirsOrThrow()
        moduleFragment.files.forEach { file ->
            val newHeaderFileName = File(file.fileEntry.name).nameWithoutExtension + ".h"
            val newCppFileName = File(file.fileEntry.name).nameWithoutExtension + ".cpp"
            val transformer = CppHeaderCreater(File(outDirFile, newHeaderFileName))
            file.acceptVoid(transformer)
            transformer.gen()
        }
    }

}