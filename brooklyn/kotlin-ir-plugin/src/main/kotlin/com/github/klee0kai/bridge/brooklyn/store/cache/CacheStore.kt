package com.github.klee0kai.bridge.brooklyn.store.cache

import com.github.klee0kai.bridge.brooklyn.model.PROJECT_FINGERPRINT_VERSION
import com.github.klee0kai.bridge.brooklyn.model.ProjectFingerPrint
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File


class CacheStore(
    val cacheFilePath: String,
) {


    suspend fun calcProjectFingerPrint(sourceFiles: List<File>): ProjectFingerPrint =
        ProjectFingerPrint(cachedFiles = FingerPrintWalker.calcFingerPrint(sourceFiles))


    suspend fun loadAndResetProjectFingerPrint(): ProjectFingerPrint? {
        val result = runCatching {
            val file = File(cacheFilePath)
            val bytes = file.readBytes()
            val fingerPrint = ProtoBuf.decodeFromByteArray<ProjectFingerPrint>(bytes)
            assert(fingerPrint.version == PROJECT_FINGERPRINT_VERSION) {
                "project cache has incorrect version actual ${fingerPrint.version} expected $PROJECT_FINGERPRINT_VERSION"
            }
            fingerPrint
        }
        File(cacheFilePath).deleteRecursively()
        return result.getOrNull()
    }


    suspend fun save(fingerPrint: ProjectFingerPrint): Result<Unit> = runCatching {
        val bytes = ProtoBuf.encodeToByteArray(fingerPrint)
        val file = File(cacheFilePath)
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeBytes(bytes)
    }


}

