package com.github.klee0kai.brooklyn.store.cache

import com.github.klee0kai.brooklyn.di.DI
import com.github.klee0kai.brooklyn.model.PROJECT_FINGERPRINT_VERSION
import com.github.klee0kai.brooklyn.model.ProjectFingerPrint
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File
import java.io.IOException


class CacheStore {

    private val ioDispatcher = DI.dispatchersModule().ioDispatcher()
    private val config by lazy { DI.config() }
    private val cacheFilePath by lazy { config.cacheFilePath }

    suspend fun calcProjectFingerPrint(sourceFiles: List<File>): ProjectFingerPrint =
        ProjectFingerPrint(cachedFiles = FingerPrintWalker.calcFingerPrint(sourceFiles))


    suspend fun loadAndResetProjectFingerPrint(): ProjectFingerPrint? = withContext(ioDispatcher) {
        val result = runCatching {
            val file = File(cacheFilePath!!)
            val bytes = file.readBytes()
            val fingerPrint = ProtoBuf.decodeFromByteArray<ProjectFingerPrint>(bytes)
            assert(fingerPrint.version == PROJECT_FINGERPRINT_VERSION) {
                "project cache has incorrect version actual ${fingerPrint.version} expected $PROJECT_FINGERPRINT_VERSION"
            }
            fingerPrint
        }
        if (!cacheFilePath.isNullOrBlank()) {
            File(cacheFilePath!!).deleteRecursively()
        }

        result.getOrNull()
    }


    suspend fun save(fingerPrint: ProjectFingerPrint): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            if (cacheFilePath.isNullOrBlank()) {
                throw IOException("cache file is blank")
            }
            val bytes = ProtoBuf.encodeToByteArray(fingerPrint)
            val file = File(cacheFilePath!!)
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeBytes(bytes)
        }
    }


}

