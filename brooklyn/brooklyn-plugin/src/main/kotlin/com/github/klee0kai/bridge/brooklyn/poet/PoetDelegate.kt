package com.github.klee0kai.bridge.brooklyn.poet


interface PoetDelegate {

    fun post(body: String): PoetDelegate

    fun pre(body: String): PoetDelegate

    fun post(body: Poet): PoetDelegate

    fun pre(body: Poet): PoetDelegate

    fun post(body: DelayedCode): PoetDelegate

    fun pre(body: DelayedCode): PoetDelegate


}

fun interface DelayedCode {
    fun provideCode(): String
}
