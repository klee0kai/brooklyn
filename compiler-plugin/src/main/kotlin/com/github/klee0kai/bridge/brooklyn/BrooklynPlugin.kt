package com.github.klee0kai.bridge.brooklyn

import org.gradle.api.Plugin
import org.gradle.api.Project

class BrooklynPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("brooklyn", BrooklynExtension::class.java)
    }

}