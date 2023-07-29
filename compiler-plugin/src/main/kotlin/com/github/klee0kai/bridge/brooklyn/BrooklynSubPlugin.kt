package com.github.klee0kai.bridge.brooklyn

import com.google.auto.service.AutoService
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption


@AutoService(KotlinCompilerPluginSupportPlugin::class)
class BrooklynSubPlugin : KotlinCompilerPluginSupportPlugin {

    companion object {
        const val GROUP_NAME = "com.github.klee0kai.bridge.brooklyn"
        const val ARTIFACT_NAME = "brooklyn"
        const val ARTIFACT_VERSION = "0.0.1"
        const val PLUGIN_ID = GROUP_NAME + ARTIFACT_NAME

    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.target.project.plugins.findPlugin(BrooklynPlugin::class.java) != null

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        return project.provider {
            listOf(SubpluginOption(key = "option-key", value = "option-value"))
            TODO()
        }
    }

    override fun getCompilerPluginId(): String = PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(GROUP_NAME, ARTIFACT_NAME, ARTIFACT_VERSION)


}