package com.klee0kai.tests.engine

import com.github.klee0kai.brooklyn.JniMirror
import com.klee0kai.tests.model.Simple

@JniMirror
object CppCallbacks {

    fun receiveSimple(simple: Simple){

    }

}