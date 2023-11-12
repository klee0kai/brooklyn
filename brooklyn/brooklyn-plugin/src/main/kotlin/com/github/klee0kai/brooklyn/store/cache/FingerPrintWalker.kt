package com.github.klee0kai.brooklyn.store.cache

import com.github.klee0kai.brooklyn.model.CachedFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File

object FingerPrintWalker {

    suspend fun calcFingerPrint(
        files: List<File>
    ): List<CachedFile> = withContext(Dispatchers.IO) {
        val mapJobs = files.map { file ->
            async {
                when {
                    file.isFile -> {
                        val hash = file.readBytes().contentHashCode()
                        listOf(
                            CachedFile(
                                path = file.absolutePath,
                                hash = hash
                            )
                        )
                    }

                    file.isDirectory -> {
                        val mapJobs = file.listFiles()?.map { child ->
                            async { calcFingerPrint(listOf(child)) }
                        }
                        mapJobs?.flatMap { it.await() } ?: emptyList()
                    }

                    else -> emptyList()
                }
            }
        }
        mapJobs.flatMap { it.await() }
    }
}