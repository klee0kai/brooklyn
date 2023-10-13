package com.klee0kai.example

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.Simple

fun main(arg: Array<String>) {
    val simple = Simple()
    SimpleJniEngine.copySimple(simple)
}