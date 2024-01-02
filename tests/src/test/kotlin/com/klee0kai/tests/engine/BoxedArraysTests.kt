package com.klee0kai.tests.engine

import com.klee0kai.tests.model.BoxedArraysModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BoxedArraysTests {

    // INTEGER
    @Test
    fun copyIntTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleIntArray = arrayOf(0, 1, 2)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableIntArray)
        assertNull(arraysModel2.nullable3IntArray)
        assertEquals(3, arraysModel2.simpleIntArray.size)
        assertEquals(0, arraysModel2.simpleIntArray[0])
        assertEquals(1, arraysModel2.simpleIntArray[1])
        assertEquals(2, arraysModel2.simpleIntArray[2])
    }

    @Test
    fun copyNullableIntTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableIntArray = arrayOf(0, 1, 2)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableIntArray!!.size)
        assertEquals(0, arraysModel2.nullableIntArray!![0])
        assertEquals(1, arraysModel2.nullableIntArray!![1])
        assertEquals(2, arraysModel2.nullableIntArray!![2])
    }

    @Test
    fun copyNullable2IntTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2IntArray = arrayOf(0, null, 2)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2IntArray!!.size)
        assertEquals(0, arraysModel2.nullable2IntArray!![0])
        assertEquals(null, arraysModel2.nullable2IntArray!![1])
        assertEquals(2, arraysModel2.nullable2IntArray!![2])
    }

    @Test
    fun copyNullable3IntTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3IntArray = arrayOf(0, null, 2)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3IntArray!!.size)
        assertEquals(0, arraysModel2.nullable3IntArray!![0])
        assertEquals(null, arraysModel2.nullable3IntArray!![1])
        assertEquals(2, arraysModel2.nullable3IntArray!![2])
    }

    // BOOLEAN
    @Test
    fun copyBoolTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleBooleanArray = arrayOf(false, true, false)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableBooleanArray)
        assertNull(arraysModel2.nullable3BooleanArray)
        assertEquals(3, arraysModel2.simpleBooleanArray.size)
        assertEquals(false, arraysModel2.simpleBooleanArray[0])
        assertEquals(true, arraysModel2.simpleBooleanArray[1])
        assertEquals(false, arraysModel2.simpleBooleanArray[2])
    }

    @Test
    fun copyNullableBoolTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableBooleanArray = arrayOf(false, true, false)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableBooleanArray!!.size)
        assertEquals(false, arraysModel2.nullableBooleanArray!![0])
        assertEquals(true, arraysModel2.nullableBooleanArray!![1])
        assertEquals(false, arraysModel2.nullableBooleanArray!![2])
    }

    @Test
    fun copyNullable2BoolTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2BooleanArray = arrayOf(false, true, null)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2BooleanArray.size)
        assertEquals(false, arraysModel2.nullable2BooleanArray[0])
        assertEquals(true, arraysModel2.nullable2BooleanArray[1])
        assertEquals(null, arraysModel2.nullable2BooleanArray[2])
    }

    @Test
    fun copyNullable3BoolTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3BooleanArray = arrayOf(false, true, null)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3BooleanArray!!.size)
        assertEquals(false, arraysModel2.nullable3BooleanArray!![0])
        assertEquals(true, arraysModel2.nullable3BooleanArray!![1])
        assertEquals(null, arraysModel2.nullable3BooleanArray!![2])
    }

    //BYTE
    @Test
    fun copyByteTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleByteArray = arrayOf(1, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableByteArray)
        assertNull(arraysModel2.nullable3ByteArray)
        assertEquals(3, arraysModel2.simpleByteArray.size)
        assertEquals(1, arraysModel2.simpleByteArray[0])
        assertEquals(2, arraysModel2.simpleByteArray[1])
        assertEquals(3, arraysModel2.simpleByteArray[2])
    }

    @Test
    fun copyNullableByteTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableByteArray = arrayOf(1, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableByteArray!!.size)
        assertEquals(1, arraysModel2.nullableByteArray!![0])
        assertEquals(2, arraysModel2.nullableByteArray!![1])
        assertEquals(3, arraysModel2.nullableByteArray!![2])
    }

    @Test
    fun copyNullable2ByteTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2ByteArray = arrayOf(1, 2, null)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2ByteArray.size)
        assertEquals(1, arraysModel2.nullable2ByteArray[0])
        assertEquals(2, arraysModel2.nullable2ByteArray[1])
        assertEquals(null, arraysModel2.nullable2ByteArray[2])
    }

    @Test
    fun copyNullable3ByteTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3ByteArray = arrayOf(null, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3ByteArray!!.size)
        assertEquals(null, arraysModel2.nullable3ByteArray!![0])
        assertEquals(2, arraysModel2.nullable3ByteArray!![1])
        assertEquals(3, arraysModel2.nullable3ByteArray!![2])
    }

    //Char
    @Test
    fun copyCharTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleCharArray = arrayOf(1.toChar(), 2.toChar(), 3.toChar())
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableCharArray)
        assertNull(arraysModel2.nullable3CharArray)
        assertEquals(3, arraysModel2.simpleCharArray.size)
        assertEquals(1.toChar(), arraysModel2.simpleCharArray[0])
        assertEquals(2.toChar(), arraysModel2.simpleCharArray[1])
        assertEquals(3.toChar(), arraysModel2.simpleCharArray[2])
    }

    @Test
    fun copyNullableCharTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableCharArray = arrayOf(1.toChar(), 2.toChar(), 3.toChar())
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableCharArray!!.size)
        assertEquals(1.toChar(), arraysModel2.nullableCharArray!![0])
        assertEquals(2.toChar(), arraysModel2.nullableCharArray!![1])
        assertEquals(3.toChar(), arraysModel2.nullableCharArray!![2])
    }

    @Test
    fun copyNullable2CharTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2CharArray = arrayOf(1.toChar(), null, 3.toChar())
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2CharArray.size)
        assertEquals(1.toChar(), arraysModel2.nullable2CharArray[0])
        assertEquals(null, arraysModel2.nullable2CharArray[1])
        assertEquals(3.toChar(), arraysModel2.nullable2CharArray[2])
    }

    @Test
    fun copyNullable3CharTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3CharArray = arrayOf(1.toChar(), null, 3.toChar())
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3CharArray!!.size)
        assertEquals(1.toChar(), arraysModel2.nullable3CharArray!![0])
        assertEquals(null, arraysModel2.nullable3CharArray!![1])
        assertEquals(3.toChar(), arraysModel2.nullable3CharArray!![2])
    }


    //Long
    @Test
    fun copyLongTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleLongArray = arrayOf(1, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableLongArray)
        assertNull(arraysModel2.nullable3LongArray)
        assertEquals(3, arraysModel2.simpleLongArray.size)
        assertEquals(1, arraysModel2.simpleLongArray[0])
        assertEquals(2, arraysModel2.simpleLongArray[1])
        assertEquals(3, arraysModel2.simpleLongArray[2])
    }

    @Test
    fun copyNullableLongTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableLongArray = arrayOf(1, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableLongArray!!.size)
        assertEquals(1, arraysModel2.nullableLongArray!![0])
        assertEquals(2, arraysModel2.nullableLongArray!![1])
        assertEquals(3, arraysModel2.nullableLongArray!![2])
    }

    @Test
    fun copyNullable2LongTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2LongArray = arrayOf(1, null, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2LongArray.size)
        assertEquals(1, arraysModel2.nullable2LongArray[0])
        assertEquals(null, arraysModel2.nullable2LongArray[1])
        assertEquals(3, arraysModel2.nullable2LongArray[2])
    }

    @Test
    fun copyNullable3LongTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3LongArray = arrayOf(1, null, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3LongArray!!.size)
        assertEquals(1, arraysModel2.nullable3LongArray!![0])
        assertEquals(null, arraysModel2.nullable3LongArray!![1])
        assertEquals(3, arraysModel2.nullable3LongArray!![2])
    }



    //Short
    @Test
    fun copyShortTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleShortArray = arrayOf(1, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableShortArray)
        assertNull(arraysModel2.nullable3ShortArray)
        assertEquals(3, arraysModel2.simpleShortArray.size)
        assertEquals(1, arraysModel2.simpleShortArray[0])
        assertEquals(2, arraysModel2.simpleShortArray[1])
        assertEquals(3, arraysModel2.simpleShortArray[2])
    }

    @Test
    fun copyNullableShortTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableShortArray = arrayOf(1, 2, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableShortArray!!.size)
        assertEquals(1, arraysModel2.nullableShortArray!![0])
        assertEquals(2, arraysModel2.nullableShortArray!![1])
        assertEquals(3, arraysModel2.nullableShortArray!![2])
    }

    @Test
    fun copyNullable2ShortTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2ShortArray = arrayOf(1, null, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2ShortArray.size)
        assertEquals(1, arraysModel2.nullable2ShortArray[0])
        assertEquals(null, arraysModel2.nullable2ShortArray[1])
        assertEquals(3, arraysModel2.nullable2ShortArray[2])
    }

    @Test
    fun copyNullable3ShortTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3ShortArray = arrayOf(1, null, 3)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3ShortArray!!.size)
        assertEquals(1, arraysModel2.nullable3ShortArray!![0])
        assertEquals(null, arraysModel2.nullable3ShortArray!![1])
        assertEquals(3, arraysModel2.nullable3ShortArray!![2])
    }


    //Float
    @Test
    fun copyFloatTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleFloatArray = arrayOf(1.1f, 2.5f, 3f)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableFloatArray)
        assertNull(arraysModel2.nullable3FloatArray)
        assertEquals(3, arraysModel2.simpleFloatArray.size)
        assertEquals(1.1f, arraysModel2.simpleFloatArray[0])
        assertEquals(2.5f, arraysModel2.simpleFloatArray[1])
        assertEquals(3f, arraysModel2.simpleFloatArray[2])
    }

    @Test
    fun copyNullableFloatTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableFloatArray = arrayOf(1.1f, 2.5f, 3f)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableFloatArray!!.size)
        assertEquals(1.1f, arraysModel2.nullableFloatArray!![0])
        assertEquals(2.5f, arraysModel2.nullableFloatArray!![1])
        assertEquals(3f, arraysModel2.nullableFloatArray!![2])
    }

    @Test
    fun copyNullable2FloatTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2FloatArray = arrayOf(1.1f, null, 3f)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2FloatArray!!.size)
        assertEquals(1.1f, arraysModel2.nullable2FloatArray!![0])
        assertEquals(null, arraysModel2.nullable2FloatArray!![1])
        assertEquals(3f, arraysModel2.nullable2FloatArray!![2])
    }

    @Test
    fun copyNullable3FloatTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3FloatArray = arrayOf(1.1f, null, 3f)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3FloatArray!!.size)
        assertEquals(1.1f, arraysModel2.nullable3FloatArray!![0])
        assertEquals(null, arraysModel2.nullable3FloatArray!![1])
        assertEquals(3f, arraysModel2.nullable3FloatArray!![2])
    }


    //Double
    @Test
    fun copyDoubleTest() {
        val arraysModel1 = BoxedArraysModel(
            simpleDoubleArray = arrayOf(1.1, 2.5, 3.0)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertNull(arraysModel2.nullableDoubleArray)
        assertNull(arraysModel2.nullable3DoubleArray)
        assertEquals(3, arraysModel2.simpleDoubleArray.size)
        assertEquals(1.1, arraysModel2.simpleDoubleArray[0])
        assertEquals(2.5, arraysModel2.simpleDoubleArray[1])
        assertEquals(3.0, arraysModel2.simpleDoubleArray[2])
    }

    @Test
    fun copyNullableDoubleTest() {
        val arraysModel1 = BoxedArraysModel(
            nullableDoubleArray = arrayOf(1.1, 2.5, 3.0)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullableDoubleArray!!.size)
        assertEquals(1.1, arraysModel2.nullableDoubleArray!![0])
        assertEquals(2.5, arraysModel2.nullableDoubleArray!![1])
        assertEquals(3.0, arraysModel2.nullableDoubleArray!![2])
    }

    @Test
    fun copyNullable2DoubleTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable2DoubleArray = arrayOf(1.1, null, 3.0)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable2DoubleArray!!.size)
        assertEquals(1.1, arraysModel2.nullable2DoubleArray!![0])
        assertEquals(null, arraysModel2.nullable2DoubleArray!![1])
        assertEquals(3.0, arraysModel2.nullable2DoubleArray!![2])
    }

    @Test
    fun copyNullable3DoubleTest() {
        val arraysModel1 = BoxedArraysModel(
            nullable3DoubleArray = arrayOf(1.1, null, 3.0)
        )

        val arraysModel2 = SimpleJniEngine.copyBoxedArrayModel(arraysModel1)

        assertEquals(3, arraysModel2.nullable3DoubleArray!!.size)
        assertEquals(1.1, arraysModel2.nullable3DoubleArray!![0])
        assertEquals(null, arraysModel2.nullable3DoubleArray!![1])
        assertEquals(3.0, arraysModel2.nullable3DoubleArray!![2])
    }
}