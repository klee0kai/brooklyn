package com.github.klee0kai.brooklyn.di

import com.github.klee0kai.brooklyn.store.cache.CacheStore
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide

@Module
abstract class StoreModule {

    @Provide(cache = Provide.CacheType.Strong)
    abstract fun cacheStore(): CacheStore

}