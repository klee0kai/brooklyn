package org.template.term.test

import com.klee0kai.tests.engine.SimpleJniEngine
import com.klee0kai.tests.mirrors.SimpleJniMirror
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArrayMirrorTests {

    @Test
    fun simpleArrayCopyTest() {
        val array1 = arrayOf(
            SimpleJniMirror(1),
            SimpleJniMirror(2),
        )

        val array2 = SimpleJniEngine.copyMirrorArray(array1)

        assertFalse(array1 === array2)
        assertEquals(2, array2.size)
        assertEquals(array1[0].id, array2[0].id)
        assertEquals(array1[1].id, array2[1].id)
    }


    @Test
    fun nullableArrayCopyTest() {
        val array1: Array<SimpleJniMirror>? = arrayOf(
            SimpleJniMirror(1),
            SimpleJniMirror(2),
        )

        val array2 = SimpleJniEngine.copyMirrorArray2(array1)

        array1!!
        array2!!
        assertFalse(array1 === array2)
        assertEquals(2, array2.size)
        assertEquals(array1[0].id, array2[0].id)
        assertEquals(array1[1].id, array2[1].id)
    }

    @Test
    fun nullable2ArrayCopyTest() {
        val array1: Array<SimpleJniMirror>? = null

        val array2 = SimpleJniEngine.copyMirrorArray2(array1)

        assertNull(array1)
        assertNull(array2)
    }

    @Test
    fun nullableItemCopyTest() {
        val array1: Array<SimpleJniMirror?> = arrayOf(
            SimpleJniMirror(1),
            null,
            SimpleJniMirror(3),
        )

        val array2 = SimpleJniEngine.copyMirrorArray3(array1)

        assertFalse(array1 === array2)
        assertEquals(3, array2.size)
        assertEquals(array1[0]?.id, array2[0]?.id)
        assertEquals(array1[1]?.id, array2[1]?.id)
        assertEquals(array1[2]?.id, array2[2]?.id)
    }

    @Test
    fun nullableCopyTest() {
        val array1: Array<SimpleJniMirror?>? = arrayOf(
            SimpleJniMirror(3),
            null,
            SimpleJniMirror(Int.MAX_VALUE),
        )

        val array2 = SimpleJniEngine.copyMirrorArray4(array1)

        array1!!
        array2!!
        assertEquals(3, array2.size)
        assertEquals(array1[0]?.id, array2[0]?.id)
        assertEquals(array1[1]?.id, array2[1]?.id)
        assertEquals(array1[2]?.id, array2[2]?.id)
    }

    @Test
    fun nullable2CopyTest() {
        val array1: Array<SimpleJniMirror?>? = null

        val array2 = SimpleJniEngine.copyMirrorArray4(array1)

        assertNull(array1)
        assertNull(array2)
    }


}