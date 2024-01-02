package com.klee0kai.tests.model

import com.github.klee0kai.brooklyn.JniPojo


@JniPojo
data class Simple @JvmOverloads constructor(
    var age: Int = 0,
    var name: String? = "foo",
    var address: String = "",
    val nested: Simple? = null,
)