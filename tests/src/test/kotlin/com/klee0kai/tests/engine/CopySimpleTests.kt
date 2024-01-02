package com.klee0kai.tests.engine

import com.klee0kai.tests.model.Simple
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CopySimpleTests {

    @Test
    fun testCopySimple() {
        val simple = Simple(2, "name", "address")
        val simple2 = SimpleJniEngine.copySimple(simple)

        assertEquals(3, simple2.age)
        assertEquals("namefrom c++", simple2.name)
        assertEquals("addressfrom c++", simple2.address)
        assertNull(simple2.nested)
    }


    @Test
    fun testCopyNestedSimple() {
        val nested = Simple(2, "name_nested", "address_nested")
        val simple = Simple(2, "name", "address", nested)
        val simple2 = SimpleJniEngine.copySimple(simple)

        assertEquals(2, simple2.nested?.age)
        assertEquals("name_nested", simple2.nested?.name)
        assertEquals("address_nested", simple2.nested?.address)
    }


}