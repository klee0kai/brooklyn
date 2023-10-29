package com.github.klee0kai.bridge.brooklyn.di

import com.github.klee0kai.bridge.brooklyn.codegen.BrooklynTypes
import com.github.klee0kai.bridge.brooklyn.controllers.CacheController
import com.github.klee0kai.bridge.brooklyn.cpp.common.CppBuildersCollection
import com.github.klee0kai.bridge.brooklyn.model.AppConfig
import com.github.klee0kai.bridge.brooklyn.store.cache.CacheStore
import com.github.klee0kai.stone.KotlinWrappersStone
import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

val DI: AppComponent by lazy { Stone.createComponent(AppComponent::class.java) }

@Component(
    wrapperProviders = [KotlinWrappersStone::class]
)
interface AppComponent {

    // modules
    fun storeModule(): StoreModule

    fun controllerModule(): ControllersModule

    fun buildersModule(): BuildersModule

    fun dispatchersModule(): DispatchesModule

    // binds
    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun config(config: AppConfig? = null): AppConfig

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun message(messageCollector: MessageCollector? = null): MessageCollector

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun context(pluginContext: IrPluginContext? = null): IrPluginContext

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun project(project: IrModuleFragment? = null): IrModuleFragment


    // provides
    fun cacheStoreLazy(): Lazy<CacheStore>

    fun brooklynTypes(): Lazy<BrooklynTypes>

    fun cppBuilderLazy(): Lazy<CppBuildersCollection>

    fun cacheControllerLazy(): Lazy<CacheController>

}