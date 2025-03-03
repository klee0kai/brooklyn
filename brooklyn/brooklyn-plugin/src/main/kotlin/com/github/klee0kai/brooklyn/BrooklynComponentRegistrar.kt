@file:OptIn(ExperimentalCompilerApi::class)

package com.github.klee0kai.brooklyn

import com.github.klee0kai.brooklyn.di.DI
import com.github.klee0kai.brooklyn.model.AppConfig
import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class BrooklynComponentRegistrar @JvmOverloads constructor(
    private val outDirFile: String = "",
    private val cacheFilePath: String = "",
    private val group: String = ""
) : CompilerPluginRegistrar() {

    override val supportsK2: Boolean = true


    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
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
            BrooklynIrGenerationExtension()
        )
    }
}

