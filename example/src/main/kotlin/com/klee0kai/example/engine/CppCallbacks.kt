package com.klee0kai.example.engine

import com.github.klee0kai.brooklyn.JniMirror
import com.klee0kai.example.model.Simple

@JniMirror
object CppCallbacks {

    fun receiveSimple(simple: Simple){

    }

}