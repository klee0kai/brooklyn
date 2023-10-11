package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.Simple
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CopySimpleTests {

    @Test
    fun testCopySimple() {
        SimpleJniEngine.initLib()

        val simple = Simple(2, "name", "address")
        val simple2 = SimpleJniEngine.copySimple(simple)

       assertEquals(3, simple2.age)
       assertEquals("namefrom c++", simple2.name)
       assertEquals("addressfrom c++", simple2.address)
    }



}