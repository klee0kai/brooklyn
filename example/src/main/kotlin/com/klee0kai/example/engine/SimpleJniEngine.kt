package com.klee0kai.example.engine

import BrooklynBridge.example.BuildConfig
import com.github.klee0kai.bridge.brooklyn.JniMirror
import com.klee0kai.example.model.NullableTypePojo
import com.klee0kai.example.model.Simple

@JniMirror
object SimpleJniEngine {

    init {
        System.load(BuildConfig.NATIVE_LIB_PATH)
    }

    external fun initLib(): Int

    external fun deinitLib(): Int

    external fun copySimple(simple: Simple): Simple

    external fun copyNullableType(simple: NullableTypePojo): NullableTypePojo

}