package com.klee0kai.tests.mirrors

import com.github.klee0kai.brooklyn.JniIgnore
import com.github.klee0kai.brooklyn.JniMirror
import kotlin.random.Random

@JniMirror
class SimpleJniMirror {

    val id = Random.nextInt()

    var someInt: Int = 0
    var someString: String = ""

    @JniIgnore
    @get:JvmName("ignoreGetter")
    val ignoreField: String get() = error("ignore")


    constructor(a: Int) {
        someInt = a
    }

    constructor(s: String) {
        someString = s
    }

    @JniIgnore
    constructor(float: Float, int: Int) {
    }


    fun inc() {
        someInt++
    }

    fun update(delta: Int, strDelta: String) {
        someInt += delta
        someString += strDelta
    }


    @JniIgnore
    @JvmName("ignoreMyMethod")
    fun ignoreMethod(): Unit = error("ignore")


    external fun incInCpp()

    external fun incInCpp2(): Int

    external fun updateFromCpp(delta: Int, strDelta: String): Int

    external fun updateFromCppDirectly(delta: Int, strDelta: String): Int

    external fun objId(): Int

}