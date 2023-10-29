package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.cpp.common.brooklynSrcFiles
import com.github.klee0kai.bridge.brooklyn.di.DI
import com.github.klee0kai.bridge.brooklyn.model.InOutFilePair
import com.github.klee0kai.bridge.brooklyn.model.ProjectFingerPrint
import org.jetbrains.kotlin.ir.declarations.path
import java.io.File

class CacheController {

    private val project by lazy { DI.project() }

    private val config by lazy { DI.config() }

    private val cacheStore by DI.cacheStoreLazy()

    private var oldFingerPrint: ProjectFingerPrint? = null

    private var newFingerPrint: ProjectFingerPrint? = null

    private var cacheDiff: Set<String>? = null

    suspend fun calcDiff() {
        oldFingerPrint = cacheStore.loadAndResetProjectFingerPrint()
        newFingerPrint = cacheStore.calcProjectFingerPrint(
            sourceFiles = project.brooklynSrcFiles
                .map { File(it.path) }
        )
        calcCachedFiles()

        // delete non cached files
        if (oldFingerPrint != null) {
            oldFingerPrint?.inOutFiles?.forEach { (inFile, outFile) ->
                if (inFile == null || !isCached(inFile)) {
                    File(outFile).deleteRecursively()
                }
            }
        } else {
            File(config.outDirFile).deleteRecursively()
        }
    }

    suspend fun saveFingerPrint(inOutFiles: List<InOutFilePair>) {
        newFingerPrint = newFingerPrint!!.copy(inOutFiles = inOutFiles)
        cacheStore.save(newFingerPrint!!)
    }

    fun isCached(path: String) = cacheDiff?.contains(path) ?: false

    private fun calcCachedFiles() {
        val cachedFiles2 = oldFingerPrint?.cachedFiles?.groupBy { it.path } ?: emptyMap()
        cacheDiff = newFingerPrint?.cachedFiles?.filter { file ->
            cachedFiles2[file.path]?.any { it.hash == file.hash } ?: false
        }
            ?.map { it.path }
            ?.toSet()
            ?: emptySet()
    }


}

