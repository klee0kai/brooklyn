package com.github.klee0kai.bridge.brooklyn

import com.github.klee0kai.bridge.brooklyn.proguard.ProguardReader
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

//@CacheableTask
open class BrooklynProguardTask : DefaultTask() {

    @get:Input
    var proguardMapFile: String? = null

    @get:Input
    var brooklynOutDir: String? = null

    @TaskAction
    fun run() {
        assert(proguardMapFile != null) { "$name : proguardMapFile not set " }
        assert(brooklynOutDir != null) { "$name : brooklynOutDir not set " }

        val proguardMap = ProguardReader.readFromMapFile(File(proguardMapFile))

    }


}