package org.template.term.test

import com.klee0kai.example.engine.SimpleJniEngine
import com.klee0kai.example.model.Simple
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArrayPojoTests {

    @Test
    fun simpleArrayCopyTest() {
        val array1 = arrayOf(
            Simple(3, "add", "name"),
            Simple(2, "add1", "name6"),
            Simple(5, "add3", "name9"),
        )

        val array2 = SimpleJniEngine.copyArray(array1)

        assertFalse(array1 === array2)
        assertEquals(3, array2.size)
        assertEquals(array1[0], array2[0])
        assertEquals(array1[1], array2[1])
        assertEquals(array1[2], array2[2])
    }


    @Test
    fun nullableArrayCopyTest() {
        val array1: Array<Simple>? = arrayOf(
            Simple(3, "add", "name"),
            Simple(Int.MIN_VALUE, "wqeqwe", "eqwew"),
            Simple(Int.MAX_VALUE, "daa", "adad"),
        )

        val array2 = SimpleJniEngine.copyArray2(array1)

        array1!!
        array2!!
        assertFalse(array1 === array2)
        assertEquals(3, array2?.size)
        assertEquals(array1[0], array2[0])
        assertEquals(array1[1], array2[1])
        assertEquals(array1[2], array2[2])
    }

    @Test
    fun nullable2ArrayCopyTest() {
        val array1: Array<Simple>? = null

        val array2 = SimpleJniEngine.copyArray2(array1)

        assertNull(array1)
        assertNull(array2)
    }

    @Test
    fun nullableItemCopyTest() {
        val array1: Array<Simple?> = arrayOf(
            Simple(3, "add", "name"),
            null,
            Simple(Int.MAX_VALUE, "daa", "adad"),
        )

        val array2 = SimpleJniEngine.copyArray3(array1)

        assertFalse(array1 === array2)
        assertEquals(3, array2.size)
        assertEquals(array1[0], array2[0])
        assertEquals(array1[1], array2[1])
        assertEquals(array1[2], array2[2])
    }

    @Test
    fun nullableCopyTest() {
        val array1: Array<Simple?>? = arrayOf(
            Simple(3, "add", "name"),
            null,
            Simple(Int.MAX_VALUE, "daa", "adad"),
        )

        val array2 = SimpleJniEngine.copyArray4(array1)

        array1!!
        array2!!
        assertEquals(3, array2.size)
        assertEquals(array1[0], array2[0])
        assertEquals(array1[1], array2[1])
        assertEquals(array1[2], array2[2])
    }

    @Test
    fun nullable2CopyTest() {
        val array1: Array<Simple?>? = null

        val array2 = SimpleJniEngine.copyArray4(array1)

        assertNull(array1)
        assertNull(array2)
    }


}