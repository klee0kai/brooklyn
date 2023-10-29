package com.github.klee0kai.bridge.brooklyn

import com.github.klee0kai.bridge.brooklyn.di.DI
import com.github.klee0kai.bridge.brooklyn.model.AppConfig
import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(ComponentRegistrar::class)
class BrooklynComponentRegistrar @JvmOverloads constructor(
    private val outDirFile: String = "",
    private val cacheFilePath: String = "",
    private val group: String = ""
) : ComponentRegistrar {


    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        val outDirFileConf = configuration.get(BrooklynCommandLineProcessor.ARG_OUT_DIR, outDirFile)
        val cacheFilePathConf = configuration.get(BrooklynCommandLineProcessor.ARG_CACHE_FILE, cacheFilePath)
        val groupConf = configuration.get(BrooklynCommandLineProcessor.ARG_GROUP, group)

        if (outDirFileConf.isBlank()) {
            messageCollector.report(CompilerMessageSeverity.ERROR, "Brooklyn: Out directory is null")
            return
        }

        DI.message(messageCollector)
        DI.config(
            AppConfig(
                outDirFile = outDirFileConf,
                cacheFilePath = cacheFilePathConf,
                group = groupConf
            )
        )

        IrGenerationExtension.registerExtension(
            project,
            BrooklynIrGenerationExtension()
        )
    }
}

