package com.klee0kai.example

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.NullableTypePojo

fun main(arg: Array<String>) {
    val simple = NullableTypePojo()
    println("init ${SimpleJniEngine.initLib()}")
    println("deinit ${SimpleJniEngine.deinitLib()}")
}