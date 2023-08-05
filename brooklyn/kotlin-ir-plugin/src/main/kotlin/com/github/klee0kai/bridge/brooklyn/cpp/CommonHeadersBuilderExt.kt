package com.github.klee0kai.bridge.brooklyn.cpp

import com.github.klee0kai.bridge.brooklyn.poet.PoetDelegate

fun <T: PoetDelegate> T.allJniHeaders() = apply {
    include("<jni.h>")
    include("<string>")
    include("<ostream>")
    include("<list>")
}