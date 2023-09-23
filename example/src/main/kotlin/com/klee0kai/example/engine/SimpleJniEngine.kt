package com.klee0kai.example.engine

import BrooklynBridge.example.BuildConfig
import com.klee0kai.example.model.Simple

object SimpleJniEngine {

    init {
        System.load(BuildConfig.NATIVE_LIB_PATH)
    }

    external fun initLib(): Int

    external fun deinitLib(): Int

    external fun copy(simple: Simple): Simple

}