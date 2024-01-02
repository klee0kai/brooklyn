package org.template.term.test

import com.klee0kai.tests.engine.SimpleJniEngine
import com.klee0kai.tests.model.NullableTypePojo
import com.klee0kai.tests.model.Simple
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CopyBoxedTypesTests {

    @Test
    fun simpleCopyTest() {
        // when
        val obj1 = NullableTypePojo(
            booleanField = true,
            charField = 'a',
            byteField = 1,
            shortField = 2,
            intField = 3,
            longField = 4,
            floatField = 5.2f,
            doubleField = 6.3,
            stringField = "str",
        )
        val obj2 = SimpleJniEngine.copyNullableType(obj1)

        //then
        assertEquals(true, obj2.booleanField)
        assertEquals('a', obj2.charField)
        assertEquals(1, obj2.byteField)
        assertEquals(2, obj2.shortField)
        assertEquals(3, obj2.intField)
        assertEquals(4, obj2.longField)
        assertEquals(5.2f, obj2.floatField)
        assertEquals(6.3, obj2.doubleField)
        assertEquals("str", obj2.stringField)
    }


    @Test
    fun simple2CopyTest() {
        // when
        val obj1 = NullableTypePojo(
            booleanField = false,
            charField = 'b',
            byteField = -1,
            shortField = -2,
            intField = -3,
            longField = -4,
            floatField = -5.2f,
            doubleField = -6.3,
            stringField = "",
            simple = Simple(1, "name", "addr"),
            simpleNullable = Simple(2, "name_nullable", "address_nullable")
        )
        val obj2 = SimpleJniEngine.copyNullableType(obj1)

        //then
        assertEquals(false, obj2.booleanField)
        assertEquals('b', obj2.charField)
        assertEquals(-1, obj2.byteField)
        assertEquals(-2, obj2.shortField)
        assertEquals(-3, obj2.intField)
        assertEquals(-4, obj2.longField)
        assertEquals(-5.2f, obj2.floatField)
        assertEquals(-6.3, obj2.doubleField)
        assertEquals("", obj2.stringField)
        assertEquals(Simple(1, "name", "addr"), obj2.simple)
        assertEquals(Simple(2, "name_nullable", "address_nullable"), obj2.simpleNullable)
    }


    @Test
    fun nullCopyTest() {
        // when
        val obj1 = NullableTypePojo()
        val obj2 = SimpleJniEngine.copyNullableType(obj1)

        //then
        assertEquals(null, obj2.booleanField)
        assertEquals(null, obj2.charField)
        assertEquals(null, obj2.byteField)
        assertEquals(null, obj2.shortField)
        assertEquals(null, obj2.intField)
        assertEquals(null, obj2.longField)
        assertEquals(null, obj2.floatField)
        assertEquals(null, obj2.doubleField)
        assertEquals(null, obj2.stringField)
        assertNull(obj2.simpleNullable)
        assertNotNull(obj2.simple)
    }


}