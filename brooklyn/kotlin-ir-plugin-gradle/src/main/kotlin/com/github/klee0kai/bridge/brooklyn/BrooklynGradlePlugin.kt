package com.github.klee0kai.bridge.brooklyn

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.io.File

class BrooklynGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project): Unit = with(target) {
        extensions.extraProperties.properties
        dependencies.add(
            "implementation",
            "${BuildConfig.KOTLIN_PLUGIN_GROUP}:annotations:${BuildConfig.KOTLIN_PLUGIN_VERSION}"
        )
        extensions.getByType(SourceSetContainer::class.java).forEach { sourceSet ->
            val extension = sourceSet.extensions.create(
                "brooklyn",
                BrooklynSourceSet::class.java,
                "${sourceSet.name} Brooklyn source set",
                project.objects
            )
            extension.outDir = File(project.buildDir, "generated/sources/brooklyn/${sourceSet.name}")
            extension.cacheFile = File(project.buildDir, "generated/sources/brooklyn/${sourceSet.name}/cache.bin")
            extension.group = project.group.toString()
        }
    }


    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        val brooklynSourceSet = kotlinCompilation.findBrooklynSourceSet()
        return brooklynSourceSet?.enabled == true
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
        val brooklynSourceSet = kotlinCompilation.findBrooklynSourceSet()

        return project.provider {
            buildList {
                brooklynSourceSet?.outDir?.path?.let { path ->
                    add(SubpluginOption(key = "outDir", value = path))
                }
                brooklynSourceSet?.cacheFile?.path?.let { path ->
                    add(SubpluginOption(key = "cacheFile", value = path))
                }
                brooklynSourceSet?.group?.let { group ->
                    add(SubpluginOption(key = "group", value = group))
                }
            }
        }
    }
}


private fun KotlinCompilation<*>.sourceSet(): SourceSet? =
    target.project.extensions
        .getByType(SourceSetContainer::class.java)
        .first { sourceSet -> sourceSet.name == defaultSourceSet.name }

private fun KotlinCompilation<*>.findBrooklynSourceSet(): BrooklynSourceSet? =
    sourceSet()
        ?.extensions
        ?.getByType(BrooklynSourceSet::class.java)

