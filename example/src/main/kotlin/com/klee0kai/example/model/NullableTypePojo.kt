package com.klee0kai.example.model

import com.github.klee0kai.bridge.brooklyn.JniPojo


//@JniPojo
data class NullableTypePojo(
    val booleanField: Boolean?,
    val charField: Char?,
    val byteField: Byte?,
    val shortField: Short?,
    val intField: Int?,
    val longField: Long?,
    val floatField: Float?,
    val doubleField: Double?,
    val numberField: Number?,
    val stringField: String?,
    val charSequenceField: CharSequence?,
)
