package com.klee0kai.example

import com.klee0kai.example.model.NullableTypePojo

object Main {

    @JvmStatic
    fun main(arg: Array<String>) {
        println("hellow world ${NullableTypePojo::class.java.name}")
    }
}