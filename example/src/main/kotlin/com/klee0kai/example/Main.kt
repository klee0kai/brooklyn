package com.klee0kai.example

import com.klee0kai.example.mirrors.SimpleJniMirror
import com.klee0kai.example.model.NullableTypePojo

object Main {

    @JvmStatic
    fun main(arg: Array<String>) {
        println("hellow world ${NullableTypePojo::class.java.name}")
        println("hellow world ${SimpleJniMirror::class.java.name}")
    }
}