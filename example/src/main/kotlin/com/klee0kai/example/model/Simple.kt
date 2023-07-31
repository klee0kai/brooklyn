package com.klee0kai.example.model

import com.github.klee0kai.bridge.brooklyn.JniPojo


@JniPojo
class Simple {
    var age: Int = 0
    var name: String? = "foo"
    var address: String = ""
}