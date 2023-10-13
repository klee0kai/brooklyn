package com.klee0kai.example.engine

import BrooklynBridge.example.BuildConfig
import com.github.klee0kai.bridge.brooklyn.Brooklyn
import com.github.klee0kai.bridge.brooklyn.JniMirror
import com.klee0kai.example.mirrors.SimpleJniMirror
import com.klee0kai.example.model.NullableTypePojo
import com.klee0kai.example.model.Simple

@JniMirror
object SimpleJniEngine {

    init {
        Brooklyn.load(BuildConfig.NATIVE_LIB_PATH)
    }

    external fun copySimple(simple: Simple): Simple

    external fun copyNullableType(simple: NullableTypePojo): NullableTypePojo

    external fun createSimpleMirror1(): SimpleJniMirror

    external fun createSimpleMirror2(): SimpleJniMirror

    external fun holdSimpleMirror(simple: SimpleJniMirror)

    external fun unHoldSimpleMirror()

}