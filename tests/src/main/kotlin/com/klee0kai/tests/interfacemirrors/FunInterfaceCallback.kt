package com.klee0kai.tests.interfacemirrors

import com.github.klee0kai.brooklyn.JniMirror

@JniMirror
fun interface FunInterfaceCallback {

    fun inc()
}