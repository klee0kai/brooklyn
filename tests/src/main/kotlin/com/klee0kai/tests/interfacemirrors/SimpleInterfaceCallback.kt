package com.klee0kai.tests.interfacemirrors

import com.github.klee0kai.brooklyn.JniMirror

@JniMirror
interface SimpleInterfaceCallback {

    fun inc()
}