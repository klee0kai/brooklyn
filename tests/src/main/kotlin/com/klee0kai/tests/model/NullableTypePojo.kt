package com.klee0kai.tests.model

import com.github.klee0kai.brooklyn.JniPojo


@JniPojo
class NullableTypePojo(
    val booleanField: Boolean? = null,
    val charField: Char? = null,
    val byteField: Byte? = null,
    val shortField: Short? = null,
    val intField: Int? = null,
    val longField: Long? = null,
    val floatField: Float? = null,
    val doubleField: Double? = null,
    val stringField: String? = null,
    val simple: Simple = Simple(),
    val simpleNullable: Simple? = null,
)
