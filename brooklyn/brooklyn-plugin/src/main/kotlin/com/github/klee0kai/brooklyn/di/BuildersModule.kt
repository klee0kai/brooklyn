package com.github.klee0kai.brooklyn.di

import com.github.klee0kai.brooklyn.codegen.BrooklynTypes
import com.github.klee0kai.brooklyn.cpp.common.CppBuildersCollection
import com.github.klee0kai.brooklyn.model.AppConfig
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import java.io.File

@Module
abstract class BuildersModule {

    @Provide(cache = Provide.CacheType.Strong)
    abstract fun kotlinVisitor(): BrooklynTypes

    @Provide(cache = Provide.CacheType.Strong)
    open fun cppBuilderCollection(appConfig: AppConfig): CppBuildersCollection {
        return CppBuildersCollection(File(appConfig.outDirFile))
    }

}