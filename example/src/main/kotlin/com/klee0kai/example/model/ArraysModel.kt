package com.klee0kai.example.model

import com.github.klee0kai.bridge.brooklyn.JniPojo

@JniPojo
class ArraysModel @JvmOverloads constructor(
    var simpleIntArray: IntArray = IntArray(0),
    var nullableIntArray: IntArray? = null,
    var booleanArray: BooleanArray = BooleanArray(0),
    var nullableBooleanArray: BooleanArray? = null,
    var byteArray: ByteArray = ByteArray(0),
    var nullableByteArray: ByteArray? = null,
    var charArray: CharArray = CharArray(0),
    var nullableCharArray: CharArray? = null,
    var longArray: LongArray = LongArray(0),
    var nullableLongArray: LongArray? = null,
    var shortArray: ShortArray = ShortArray(0),
    var nullableShortArray: ShortArray? = null,
    var floatArray: FloatArray = FloatArray(0),
    var nullableFloatArray: FloatArray? = null,
    var doubleArray: DoubleArray = DoubleArray(0),
    var nullableDoubleArray: DoubleArray? = null,

    var simpleStringArray: Array<String> = emptyArray(),
    var nullableStringArray: Array<String>? = null,
    var nullableStringArray2: Array<String?> = emptyArray(),
    var nullableStringArray3: Array<String?>? = null,
)
