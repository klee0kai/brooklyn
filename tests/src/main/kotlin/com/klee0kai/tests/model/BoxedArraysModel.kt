package com.klee0kai.tests.model

import com.github.klee0kai.brooklyn.JniPojo

@JniPojo
class BoxedArraysModel @JvmOverloads constructor(
    var simpleIntArray: Array<Int> = emptyArray(),
    var nullableIntArray: Array<Int>? = null,
    var nullable2IntArray: Array<Int?> = emptyArray(),
    var nullable3IntArray: Array<Int?>? = null,

    var simpleBooleanArray: Array<Boolean> = emptyArray(),
    var nullableBooleanArray: Array<Boolean>? = null,
    var nullable2BooleanArray: Array<Boolean?> = emptyArray(),
    var nullable3BooleanArray: Array<Boolean?>? = null,

    var simpleByteArray: Array<Byte> = emptyArray(),
    var nullableByteArray: Array<Byte>? = null,
    var nullable2ByteArray: Array<Byte?> = emptyArray(),
    var nullable3ByteArray: Array<Byte?>? = null,

    var simpleCharArray: Array<Char> = emptyArray(),
    var nullableCharArray: Array<Char>? = null,
    var nullable2CharArray: Array<Char?> = emptyArray(),
    var nullable3CharArray: Array<Char?>? = null,

    var simpleLongArray: Array<Long> = emptyArray(),
    var nullableLongArray: Array<Long>? = null,
    var nullable2LongArray: Array<Long?> = emptyArray(),
    var nullable3LongArray: Array<Long?>? = null,

    var simpleShortArray: Array<Short> = emptyArray(),
    var nullableShortArray: Array<Short>? = null,
    var nullable2ShortArray: Array<Short?> = emptyArray(),
    var nullable3ShortArray: Array<Short?>? = null,

    var simpleFloatArray: Array<Float> = emptyArray(),
    var nullableFloatArray: Array<Float>? = null,
    var nullable2FloatArray: Array<Float?> = emptyArray(),
    var nullable3FloatArray: Array<Float?>? = null,

    var simpleDoubleArray: Array<Double> = emptyArray(),
    var nullableDoubleArray: Array<Double>? = null,
    var nullable2DoubleArray: Array<Double?> = emptyArray(),
    var nullable3DoubleArray: Array<Double?>? = null,
)
