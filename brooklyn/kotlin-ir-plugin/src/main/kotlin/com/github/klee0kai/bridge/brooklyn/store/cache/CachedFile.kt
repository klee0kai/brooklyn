package com.github.klee0kai.bridge.brooklyn.store.cache

import kotlinx.serialization.Serializable
import java.io.File

const val PROJECT_FINGERPRINT_VERSION = 0

@Serializable
data class CachedFile(
    val path: String,
    val hash: Int,
)


@Serializable
data class ProjectFingerPrint(
    val version: Int = PROJECT_FINGERPRINT_VERSION,
    val cachedFiles: List<CachedFile>
)

data class ProjectDiff(
    val nonChangedFiles: Set<File>
)

