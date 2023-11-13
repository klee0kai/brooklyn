package com.github.klee0kai.brooklyn.cpp.common

import com.github.klee0kai.brooklyn.poet.PoetDelegate

fun <T : PoetDelegate> T.allJniHeaders() = apply {
    include("<jni.h>")
    include("<string>")
    include("<ostream>")
    include("<list>")
    include("<vector>")
    include("<memory>")
    include("<map>")
}