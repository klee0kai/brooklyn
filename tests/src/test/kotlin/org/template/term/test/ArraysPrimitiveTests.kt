package org.template.term.test

import com.klee0kai.tests.engine.SimpleJniEngine
import com.klee0kai.tests.model.ArraysModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ArraysPrimitiveTests {

    @Test
    fun copyIntTest() {
        val arraysModel1 = ArraysModel(
            simpleIntArray = IntArray(3) { it }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableIntArray)
        assertEquals(3, arraysModel2.simpleIntArray.size)
        assertEquals(0, arraysModel2.simpleIntArray[0])
        assertEquals(1, arraysModel2.simpleIntArray[1])
        assertEquals(2, arraysModel2.simpleIntArray[2])
    }

    @Test
    fun copyNullableIntTest() {
        val arraysModel1 = ArraysModel(
            nullableIntArray = IntArray(3) { it }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableIntArray!!.size)
        assertEquals(0, arraysModel2.nullableIntArray!![0])
        assertEquals(1, arraysModel2.nullableIntArray!![1])
        assertEquals(2, arraysModel2.nullableIntArray!![2])
    }


    @Test
    fun copyBooleanTest() {
        val arraysModel1 = ArraysModel(
            booleanArray = BooleanArray(3) { it == 1 }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableBooleanArray)
        assertEquals(3, arraysModel2.booleanArray.size)
        assertEquals(false, arraysModel2.booleanArray[0])
        assertEquals(true, arraysModel2.booleanArray[1])
        assertEquals(false, arraysModel2.booleanArray[2])
    }

    @Test
    fun copyNullableBooleanTest() {
        val arraysModel1 = ArraysModel(
            nullableBooleanArray = BooleanArray(3) { it == 1 }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableBooleanArray!!.size)
        assertEquals(false, arraysModel2.nullableBooleanArray!![0])
        assertEquals(true, arraysModel2.nullableBooleanArray!![1])
        assertEquals(false, arraysModel2.nullableBooleanArray!![2])
    }


    @Test
    fun copyByteTest() {
        val arraysModel1 = ArraysModel(
            byteArray = ByteArray(3) { it.toByte() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableByteArray)
        assertEquals(3, arraysModel2.byteArray.size)
        assertEquals(0, arraysModel2.byteArray[0])
        assertEquals(1, arraysModel2.byteArray[1])
        assertEquals(2, arraysModel2.byteArray[2])
    }


    @Test
    fun copyNullableByteTest() {
        val arraysModel1 = ArraysModel(
            nullableByteArray = ByteArray(3) { it.toByte() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableByteArray!!.size)
        assertEquals(0, arraysModel2.nullableByteArray!![0])
        assertEquals(1, arraysModel2.nullableByteArray!![1])
        assertEquals(2, arraysModel2.nullableByteArray!![2])
    }


    @Test
    fun copyCharTest() {
        val arraysModel1 = ArraysModel(
            charArray = CharArray(3) { it.toChar() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableCharArray)
        assertEquals(3, arraysModel2.charArray.size)
        assertEquals(0.toChar(), arraysModel2.charArray[0])
        assertEquals(1.toChar(), arraysModel2.charArray[1])
        assertEquals(2.toChar(), arraysModel2.charArray[2])
    }

    @Test
    fun copyNullableCharTest() {
        val arraysModel1 = ArraysModel(
            nullableCharArray = CharArray(3) { it.toChar() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableCharArray!!.size)
        assertEquals(0.toChar(), arraysModel2.nullableCharArray!![0])
        assertEquals(1.toChar(), arraysModel2.nullableCharArray!![1])
        assertEquals(2.toChar(), arraysModel2.nullableCharArray!![2])
    }


    @Test
    fun copyLongTest() {
        val arraysModel1 = ArraysModel(
            longArray = LongArray(3) { it.toLong() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableLongArray)
        assertEquals(3, arraysModel2.longArray.size)
        assertEquals(0L, arraysModel2.longArray[0])
        assertEquals(1L, arraysModel2.longArray[1])
        assertEquals(2L, arraysModel2.longArray[2])
    }

    @Test
    fun copyNullableLongTest() {
        val arraysModel1 = ArraysModel(
            nullableLongArray = LongArray(3) { it.toLong() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableLongArray!!.size)
        assertEquals(0L, arraysModel2.nullableLongArray!![0])
        assertEquals(1L, arraysModel2.nullableLongArray!![1])
        assertEquals(2L, arraysModel2.nullableLongArray!![2])
    }


    @Test
    fun copyShortTest() {
        val arraysModel1 = ArraysModel(
            shortArray = ShortArray(3) { it.toShort() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableShortArray)
        assertEquals(3, arraysModel2.shortArray.size)
        assertEquals(0.toShort(), arraysModel2.shortArray[0])
        assertEquals(1.toShort(), arraysModel2.shortArray[1])
        assertEquals(2.toShort(), arraysModel2.shortArray[2])
    }

    @Test
    fun copyNullableShortTest() {
        val arraysModel1 = ArraysModel(
            nullableShortArray = ShortArray(3) { it.toShort() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableShortArray!!.size)
        assertEquals(0.toShort(), arraysModel2.nullableShortArray!![0])
        assertEquals(1.toShort(), arraysModel2.nullableShortArray!![1])
        assertEquals(2.toShort(), arraysModel2.nullableShortArray!![2])
    }


    @Test
    fun copyFloatTest() {
        val arraysModel1 = ArraysModel(
            floatArray = FloatArray(3) { it.toFloat() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableFloatArray)
        assertEquals(3, arraysModel2.floatArray.size)
        assertEquals(0f, arraysModel2.floatArray[0])
        assertEquals(1f, arraysModel2.floatArray[1])
        assertEquals(2f, arraysModel2.floatArray[2])
    }

    @Test
    fun copyNullableFloatTest() {
        val arraysModel1 = ArraysModel(
            nullableFloatArray = FloatArray(3) { it.toFloat() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableFloatArray!!.size)
        assertEquals(0f, arraysModel2.nullableFloatArray!![0])
        assertEquals(1f, arraysModel2.nullableFloatArray!![1])
        assertEquals(2f, arraysModel2.nullableFloatArray!![2])
    }


    @Test
    fun copyDoubleTest() {
        val arraysModel1 = ArraysModel(
            doubleArray = DoubleArray(3) { it.toDouble() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableDoubleArray)
        assertEquals(3, arraysModel2.doubleArray.size)
        assertEquals(0.0, arraysModel2.doubleArray[0])
        assertEquals(1.0, arraysModel2.doubleArray[1])
        assertEquals(2.0, arraysModel2.doubleArray[2])
    }


    @Test
    fun copyNullableDoubleTest() {
        val arraysModel1 = ArraysModel(
            nullableDoubleArray = DoubleArray(3) { it.toDouble() }
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableDoubleArray!!.size)
        assertEquals(0.0, arraysModel2.nullableDoubleArray!![0])
        assertEquals(1.0, arraysModel2.nullableDoubleArray!![1])
        assertEquals(2.0, arraysModel2.nullableDoubleArray!![2])
    }


    @Test
    fun copySimpleStringArray() {
        val arraysModel1 = ArraysModel(
            simpleStringArray = arrayOf("a", "b", "c")
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableStringArray)
        assertNull(arraysModel2.nullableStringArray3)
        assertEquals(3, arraysModel2.simpleStringArray.size)
        assertEquals("a", arraysModel2.simpleStringArray[0])
        assertEquals("b", arraysModel2.simpleStringArray[1])
        assertEquals("c", arraysModel2.simpleStringArray[2])
    }

    @Test
    fun copyNullableStringArray() {
        val arraysModel1 = ArraysModel(
            nullableStringArray = arrayOf("a", "b", "c")
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableStringArray!!.size)
        assertEquals("a", arraysModel2.nullableStringArray!![0])
        assertEquals("b", arraysModel2.nullableStringArray!![1])
        assertEquals("c", arraysModel2.nullableStringArray!![2])
    }

    @Test
    fun copyNullable2StringArray() {
        val arraysModel1 = ArraysModel(
            nullableStringArray2 = arrayOf("a", null, "c")
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableStringArray2.size)
        assertEquals("a", arraysModel2.nullableStringArray2[0])
        assertEquals(null, arraysModel2.nullableStringArray2[1])
        assertEquals("c", arraysModel2.nullableStringArray2[2])
    }

    @Test
    fun copyNullable3StringArray() {
        val arraysModel1 = ArraysModel(
            nullableStringArray3 = arrayOf("a", null, "c")
        )

        val arraysModel2 = SimpleJniEngine.copyArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableStringArray3!!.size)
        assertEquals("a", arraysModel2.nullableStringArray3!![0])
        assertEquals(null, arraysModel2.nullableStringArray3!![1])
        assertEquals("c", arraysModel2.nullableStringArray3!![2])
    }

}