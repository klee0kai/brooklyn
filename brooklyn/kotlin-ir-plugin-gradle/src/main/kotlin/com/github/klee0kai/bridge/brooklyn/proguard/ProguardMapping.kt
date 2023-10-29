package com.github.klee0kai.bridge.brooklyn.proguard

data class ProguardMapping(
    val fullNameOriginal: String,
    val obfuscatedName: String,
)

data class ProguardClassMapping(
    val fullNameOriginal: String,
    val obfuscatedName: String,
    val methods: MutableList<ProguardMapping> = mutableListOf(),
    val fields: MutableList<ProguardMapping> = mutableListOf(),
)
