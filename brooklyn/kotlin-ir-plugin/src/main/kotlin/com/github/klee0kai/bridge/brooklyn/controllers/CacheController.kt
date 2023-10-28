package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.di.DI
import com.github.klee0kai.bridge.brooklyn.store.cache.ProjectDiff
import com.github.klee0kai.bridge.brooklyn.store.cache.ProjectFingerPrint
import com.github.klee0kai.bridge.brooklyn.store.cache.minus
import org.jetbrains.kotlin.ir.declarations.path
import java.io.File

class CacheController {

    private val config by lazy { DI.config() }

    private val project by lazy { DI.project() }

    private val cacheStore by DI.cacheStoreLazy()

    private var oldFingerPrint: ProjectFingerPrint? = null

    private var newFingerPrint: ProjectFingerPrint? = null

    var cacheDiff: ProjectDiff? = null
        private set


    suspend fun calcDiff() {
        oldFingerPrint = cacheStore.loadAndResetProjectFingerPrint()
        newFingerPrint = cacheStore.calcProjectFingerPrint(project.files.map { File(it.path) })
        cacheDiff = newFingerPrint!! - oldFingerPrint

        // delete non cached files
        File(config.outDirFile).deleteRecursively()
    }

    suspend fun saveFingerPrint() {
        cacheStore.save(newFingerPrint!!)
    }

}