package com.github.klee0kai.brooklyn.controllers

import com.github.klee0kai.brooklyn.di.DI

class CacheController {

    private val project by lazy { DI.project() }

    private val config by lazy { DI.config() }

    private val cacheStore by DI.cacheStoreLazy()

    suspend fun loadFingerPrint() = cacheStore.loadProjectFingerPrint()

    suspend fun saveFingerPrint() = cacheStore.save()

}

