package com.klee0kai.example.mirrors

import com.github.klee0kai.bridge.brooklyn.JniMirror

@JniMirror
object JniCounter {

    var count: Int = 0

    fun inc() {
        count++
    }

}