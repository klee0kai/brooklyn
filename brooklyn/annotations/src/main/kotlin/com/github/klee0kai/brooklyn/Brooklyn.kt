package com.github.klee0kai.brooklyn

object Brooklyn {

    fun load(filename: String) {
        System.load(filename)
        val result = initLib()
        if (result != 0) error("brooklyn init error $result")
    }

    fun loadLibrary(libname: String) {
        System.loadLibrary(libname)
        val result = initLib()
        if (result != 0) error("brooklyn init error $result")
    }

    fun deinit() {
        val result = deInitLib()
        if (result != 0) error("brooklyn init error $result")
    }

    external fun initLib(): Int

    external fun deInitLib(): Int

}