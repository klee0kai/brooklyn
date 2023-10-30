package com.github.klee0kai.bridge.brooklyn

import java.io.File

abstract class BrooklynPluginExtension {

    var enabled: Boolean = false

    var outDir: File? = null

    var cacheFile: File? = null

    var group: String? = null

}