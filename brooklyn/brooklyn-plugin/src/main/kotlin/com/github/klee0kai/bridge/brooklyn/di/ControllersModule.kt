package com.github.klee0kai.bridge.brooklyn.di

import com.github.klee0kai.bridge.brooklyn.controllers.*
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide

@Module
interface ControllersModule {

    @Provide(cache = Provide.CacheType.Soft)
    fun cacheController(): CacheController

    @Provide(cache = Provide.CacheType.Soft)
    fun commonGenController(): CommonGenController

    @Provide(cache = Provide.CacheType.Soft)
    fun indexGenController(): IndexesController

    @Provide(cache = Provide.CacheType.Soft)
    fun mapperGenController(): MappersGenController

    @Provide(cache = Provide.CacheType.Soft)
    fun mirrorGenController(): MirrorsGenController

    @Provide(cache = Provide.CacheType.Soft)
    fun modelGenController(): ModelsGenController

    @Provide(cache = Provide.CacheType.Soft)
    fun cmakeGenController():CmakeGenController


}