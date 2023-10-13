package com.github.klee0kai.bridge.brooklyn

object Brooklyn {

    fun load(filename: String) {
        System.load(filename)
        val result = initLib()
        if (result != 0) error("brooklyn init error $result")
    }

    fun deinit() {
        val result = deInitLib()
        if (result != 0) error("brooklyn init error $result")
    }

    private external fun initLib(): Int

    private external fun deInitLib(): Int

}