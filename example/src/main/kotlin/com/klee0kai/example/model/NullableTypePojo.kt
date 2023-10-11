package com.klee0kai.example.model

import com.github.klee0kai.bridge.brooklyn.JniPojo


@JniPojo
data class NullableTypePojo(
    val booleanField: Boolean? = null,
    val charField: Char? = null,
    val byteField: Byte? = null,
    val shortField: Short? = null,
    val intField: Int? = null,
    val longField: Long? = null,
    val floatField: Float? = null,
    val doubleField: Double? = null,
    val stringField: String? = null,
)
