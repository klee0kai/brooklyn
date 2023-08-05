package com.klee0kai.example.model

import com.github.klee0kai.bridge.brooklyn.JniPojo


@JniPojo
data class NullableTypePojo(
    val unit: Unit?,
    val boolean: Boolean?,
    val char: Char?,
    val byte: Byte?,
    val short: Short?,
    val int: Int?,
    val long: Long?,
    val float: Float?,
    val double: Double?,
    val number: Number?,
    val string: String?,
    val charSequence: CharSequence?,
)
