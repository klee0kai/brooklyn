package com.klee0kai.tests.interface_mirrors

import com.klee0kai.tests.engine.SimpleJniEngine
import com.klee0kai.tests.interfacemirrors.FunInterfaceCallback
import com.klee0kai.tests.interfacemirrors.InterfacesEngine
import com.klee0kai.tests.interfacemirrors.SimpleInterfaceCallback
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class InterfaceCallbackTests {

    init {
        SimpleJniEngine
    }

    @Test
    fun simpleMirrorIncFromCpp() {
        var counter = 0
        val callback = object : SimpleInterfaceCallback {
            override fun inc() {
                counter++
            }
        }
        InterfacesEngine.callSimple(callback)

        Assertions.assertEquals(1, counter)
    }

    @Test
    fun funMirrorIncFromCpp() {
        var counter = 0
        val callback = FunInterfaceCallback {
            counter++
        }
        InterfacesEngine.callFun(callback)

        Assertions.assertEquals(1, counter)
    }
}