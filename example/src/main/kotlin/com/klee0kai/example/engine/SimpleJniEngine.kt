package com.klee0kai.example.engine

import BrooklynBridge.example.BuildConfig
import com.github.klee0kai.bridge.brooklyn.Brooklyn
import com.github.klee0kai.bridge.brooklyn.JniMirror
import com.klee0kai.example.mirrors.SimpleJniMirror
import com.klee0kai.example.model.ArraysModel
import com.klee0kai.example.model.BoxedArraysModel
import com.klee0kai.example.model.NullableTypePojo
import com.klee0kai.example.model.Simple

@JniMirror
object SimpleJniEngine {

    init {
        Brooklyn.load(BuildConfig.NATIVE_LIB_PATH)
    }

    external fun copySimple(simple: Simple): Simple

    external fun copyArray(simpleArray: Array<Simple>): Array<Simple>
    external fun copyArray2(simpleArray: Array<Simple>?): Array<Simple>?
    external fun copyArray3(simpleArray: Array<Simple?>): Array<Simple?>
    external fun copyArray4(simpleArray: Array<Simple?>?): Array<Simple?>?

    external fun copyMirrorArray(simpleArray: Array<SimpleJniMirror>): Array<SimpleJniMirror>
    external fun copyMirrorArray2(simpleArray: Array<SimpleJniMirror>?): Array<SimpleJniMirror>?
    external fun copyMirrorArray3(simpleArray: Array<SimpleJniMirror?>): Array<SimpleJniMirror?>
    external fun copyMirrorArray4(simpleArray: Array<SimpleJniMirror?>?): Array<SimpleJniMirror?>?

    external fun copyNullableType(simple: NullableTypePojo): NullableTypePojo

    external fun copyArrayModel(arrays: ArraysModel): ArraysModel

    external fun copyBoxedArrayModel(arrays: BoxedArraysModel): BoxedArraysModel

    external fun createSimpleMirror1(): SimpleJniMirror

    external fun createSimpleMirror2(): SimpleJniMirror

    external fun holdSimpleMirror(simple: SimpleJniMirror)

    external fun unHoldSimpleMirror()

}