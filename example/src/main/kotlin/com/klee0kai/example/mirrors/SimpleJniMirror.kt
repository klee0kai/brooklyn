package com.klee0kai.example.mirrors

import com.github.klee0kai.bridge.brooklyn.JniMirror

@JniMirror
class SimpleJniMirror {

    var someInt: Int = 0
    var someString: String = ""

    constructor(a: Int) {
        someInt = a
    }

    constructor(s: String) {
        someString = s
    }


    fun inc() {
        someInt++
    }

    fun update(delta: Int, strDelta: String) {
        someInt += delta
        someString += strDelta
    }

    external fun incInCpp()

    external fun incInCpp2(): Int

    external fun updateInCpp(delta: Int, strDelta: String): Int

}