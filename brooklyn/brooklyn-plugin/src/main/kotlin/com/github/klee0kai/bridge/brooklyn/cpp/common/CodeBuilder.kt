package com.github.klee0kai.bridge.brooklyn.cpp.common

import com.github.klee0kai.bridge.brooklyn.poet.Poet
import com.github.klee0kai.bridge.brooklyn.poet.PoetDelegate
import java.io.File

class CodeBuilder(
    val file: File,
    val header: Poet = Poet(),
    val variables: Poet = Poet(),
    val footer: Poet = Poet(),
    val body: Poet = Poet()
) : PoetDelegate by body {

    private val delayed = mutableListOf<CodeBuilder.() -> Unit>()

    fun header(block: Poet.() -> Unit) = apply { header.apply(block) }
    fun variables(block: Poet.() -> Unit) = apply { variables.apply(block) }
    fun body(block: Poet.() -> Unit) = apply { body.apply(block) }
    fun footer(block: Poet.() -> Unit) = apply { footer.apply(block) }

    fun delayed(block: CodeBuilder.() -> Unit) = apply { delayed.add(block) }

    fun collectDelayed() {
        val delayed = this.delayed.toList()
        this.delayed.clear()
        delayed.forEach { it.invoke(this) }
        if (delayed.isNotEmpty()) collectDelayed()
    }

    fun gen(sym: String = "//") {
        collectDelayed()
        file.parentFile.mkdirs()
        file.writeBytes(
            Poet()
                .brooklynHeaderComment(sym)
                .post(header)
                .lines(1)
                .post(variables)
                .lines(1)
                .post(body)
                .lines(1)
                .post(footer)
                .toString()
                .encodeToByteArray()
        )
    }


}