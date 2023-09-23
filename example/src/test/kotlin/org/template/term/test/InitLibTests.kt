package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.Simple
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InitLibTests {

    @Test
    fun initTest() {
        //when
        val initResult = SimpleJniEngine.initLib()

        assertEquals(0, initResult, "Init should return 0, if init is correct")
    }

    @Test
    fun testCopy() {
        SimpleJniEngine.initLib()

        val simple = Simple(2, "name", "address")
        val simple2 = SimpleJniEngine.copy(simple)

        assertEquals(3, simple2.age)
        assertEquals("namefrom c++", simple2.name)
        assertEquals("addressfrom c++", simple2.address)
    }

}