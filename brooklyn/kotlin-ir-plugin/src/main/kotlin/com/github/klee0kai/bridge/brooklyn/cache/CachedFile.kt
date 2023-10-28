package com.github.klee0kai.bridge.brooklyn.cache

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

operator fun ProjectFingerPrint.minus(fingerPrint: ProjectFingerPrint?): ProjectDiff {
    val cachedFiles2 = fingerPrint?.cachedFiles?.groupBy { it.path } ?: emptyMap()
    return cachedFiles.filter { file ->
        cachedFiles2[file.path]?.any { it.hash == file.hash } ?: false
    }
        .map { File(it.path) }
        .toSet()
        .let { ProjectDiff(it) }
}