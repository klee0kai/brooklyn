package com.github.klee0kai.bridge.brooklyn

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.io.File

class BrooklynGradlePlugin : KotlinCompilerPluginSupportPlugin {

    private var extension: BrooklynPluginExtension? = null

    override fun apply(target: Project): Unit = with(target) {
        extensions.extraProperties.properties
        dependencies.add(
            "implementation",
            "${BuildConfig.KOTLIN_PLUGIN_GROUP}:annotations:${BuildConfig.KOTLIN_PLUGIN_VERSION}"
        )

        extension = extensions.create("brooklyn", BrooklynPluginExtension::class.java)
        extension?.group = null
    }


    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return extension != null
    }

    override fun getCompilerPluginId(): String = BuildConfig.KOTLIN_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = BuildConfig.KOTLIN_PLUGIN_GROUP,
        artifactId = BuildConfig.KOTLIN_PLUGIN_NAME,
        version = BuildConfig.KOTLIN_PLUGIN_VERSION
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val brooklynExtension = extension ?: return project.provider { emptyList() }

        val sourceSetGenerated = File(project.buildDir, "generated/sources/${kotlinCompilation.defaultSourceSet.name}")
        val outDir = File(sourceSetGenerated, extension?.outDir?.path ?: "brooklyn")
        val cacheFile = File(sourceSetGenerated, extension?.cacheFile?.path ?: "brooklyn/cache.bin")

        val group = brooklynExtension.group ?: project.group.toString()
        return project.provider {
            buildList {
                add(SubpluginOption(key = "outDir", value = outDir.path))
                add(SubpluginOption(key = "cacheFile", value = cacheFile.path))
                add(SubpluginOption(key = "group", value = group))
            }
        }
    }
}

