package com.klee0kai.tests.engine

import com.github.klee0kai.brooklyn.Brooklyn
import com.github.klee0kai.brooklyn.JniMirror
import com.klee0kai.tests.mirrors.SimpleJniMirror
import com.klee0kai.tests.model.ArraysModel
import com.klee0kai.tests.model.BoxedArraysModel
import com.klee0kai.tests.model.NullableTypePojo
import com.klee0kai.tests.model.Simple
import com.klee_kai.tests.tests.BuildConfig

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

    external fun setCounterTo102()
    external fun counterIncrement()

}