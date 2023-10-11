package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.NullableTypePojo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CopyBoxedTypesTests {

    @Test
    fun simpleCopyTest() {
        // given
        SimpleJniEngine.initLib()

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
        // given
        SimpleJniEngine.initLib()

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
    }


    @Test
    fun nullCopyTest() {
        // given
        SimpleJniEngine.initLib()

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
    }


}