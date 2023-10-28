package com.github.klee0kai.bridge.brooklyn.di

import com.github.klee0kai.bridge.brooklyn.codegen.KotlinVisitor
import com.github.klee0kai.bridge.brooklyn.cpp.common.CppBuildersCollection
import com.github.klee0kai.bridge.brooklyn.model.AppConfig
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import java.io.File

@Module
abstract class BuildersModule {

    @Provide(cache = Provide.CacheType.Strong)
    abstract fun kotlinVisitor(): KotlinVisitor

    @Provide(cache = Provide.CacheType.Strong)
    open fun cppBuilderCollection(appConfig: AppConfig): CppBuildersCollection {
        return CppBuildersCollection(File(appConfig.outDirFile))
    }

}