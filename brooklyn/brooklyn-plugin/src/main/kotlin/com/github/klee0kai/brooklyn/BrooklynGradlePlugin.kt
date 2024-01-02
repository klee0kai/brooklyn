package com.github.klee0kai.brooklyn

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
        extension?.outDir = File("brooklyn")
        extension?.cacheFile = File("brooklyn/cache.bin")
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
        val outDir = extension?.outDir?.let { sourceSetGenerated.resolve(it) }
        val cacheFile = extension?.cacheFile?.let { sourceSetGenerated.resolve(it) }

        val group = brooklynExtension.group ?: project.group.toString()
        return project.provider {
            buildList {
                outDir?.let { add(SubpluginOption(key = "outDir", value = outDir.path)) }
                cacheFile?.let { add(SubpluginOption(key = "cacheFile", value = cacheFile.path)) }
                add(SubpluginOption(key = "group", value = group))
            }
        }
    }
}

