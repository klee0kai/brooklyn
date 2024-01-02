package com.klee0kai.tests

import com.klee0kai.tests.engine.SimpleJniEngine
import com.klee0kai.tests.model.Simple

fun main(arg: Array<String>) {
    val simple = Simple()
    SimpleJniEngine.copySimple(simple)
}