package com.github.klee0kai.bridge.brooklyn.di

import com.github.klee0kai.bridge.brooklyn.model.AppConfig
import com.github.klee0kai.bridge.brooklyn.store.cache.CacheStore
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide

@Module
open class StoreModule {

    @Provide(cache = Provide.CacheType.Strong)
    open fun cacheStore(appConfig: AppConfig): CacheStore {
        return CacheStore(appConfig.cacheFilePath)
    }

}