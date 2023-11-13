package com.github.klee0kai.brooklyn.model

data class AppConfig(
    val outDirFile: String = "",
    val cacheFilePath: String? = null,
    val group: String? = null,
)
