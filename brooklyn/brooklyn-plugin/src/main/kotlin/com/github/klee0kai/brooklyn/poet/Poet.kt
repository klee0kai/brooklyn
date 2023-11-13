package com.github.klee0kai.brooklyn.poet


class Poet(
    code: String? = null
) : PoetDelegate {

    private val poets = mutableListOf<Any?>()
    val metas = mutableMapOf<String, Any>()

    init {
        if (code != null) poets.add(code)
    }

    override fun post(body: String) = apply { poets.add(body) }

    override fun pre(body: String) = apply { poets.add(0, body) }

    override fun post(body: Poet) = apply { poets.add(body) }

    override fun pre(body: Poet) = apply { poets.add(0, body) }


    override fun post(body: DelayedCode) = apply { poets.add(body) }

    override fun pre(body: DelayedCode) = apply { poets.add(0, body) }


    override fun toString(): String = buildString {
        poets.forEach {
            when (it) {
                is String -> append(it)
                is Poet -> append(it.toString())
                is DelayedCode -> append(it.provideCode())
            }
        }

    }

}

