package com.github.klee0kai.brooklyn.model

import kotlinx.serialization.Serializable

const val PROJECT_FINGERPRINT_VERSION = 0

@Serializable
data class CachedFile(
    val path: String,
    val hash: Int,
)

@Serializable
data class InOutFilePair(
    val inFile: String?,
    val outFile: String,
)

@Serializable
data class ProjectFingerPrint(
    val version: Int = PROJECT_FINGERPRINT_VERSION,
    val cachedFiles: List<CachedFile> = emptyList(),
    val inOutFiles: List<InOutFilePair> = emptyList()
)


