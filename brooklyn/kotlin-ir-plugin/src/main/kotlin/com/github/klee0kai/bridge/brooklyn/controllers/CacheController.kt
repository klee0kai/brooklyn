package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.di.DI
import com.github.klee0kai.bridge.brooklyn.store.cache.ProjectFingerPrint
import org.jetbrains.kotlin.ir.declarations.path
import java.io.File

class CacheController {

    private val config by lazy { DI.config() }

    private val project by lazy { DI.project() }

    private val cacheStore by DI.cacheStoreLazy()

    private var oldFingerPrint: ProjectFingerPrint? = null

    private var newFingerPrint: ProjectFingerPrint? = null

    private var cacheDiff: Set<String>? = null

    suspend fun calcDiff() {
        oldFingerPrint = cacheStore.loadAndResetProjectFingerPrint()
        newFingerPrint = cacheStore.calcProjectFingerPrint(project.files.map { File(it.path) })
        calcCachedFiles()

        // delete non cached files
        project.files.forEach { file ->
            if (!isCached(file.path)) {
                File(file.path).deleteRecursively()
            }
        }
    }

    suspend fun saveFingerPrint() {
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