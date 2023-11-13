package com.github.klee0kai.brooklyn.di

import com.github.klee0kai.stone.annotations.module.Module
import kotlinx.coroutines.Dispatchers

@Module
open class DispatchesModule {

    open fun ioDispatcher() = Dispatchers.IO

    open fun defaultDispatcher() = Dispatchers.Default

}