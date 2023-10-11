package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun CodeBuilder.initStdTypes(isImpl: Boolean = false) = apply {
    val declare = "int initStdTypes(JNIEnv *env)"
    if (!isImpl) {
        body { statement(declare) }
        return@apply
    }
    variables {
        stdTypeIndexStructure("booleanIndex")
    }

    body {
        lines(1)
        line("${declare}{")
        booleanIndexInit()
        line("}")
    }
}

fun CodeBuilder.deinitStdTypes(isImpl: Boolean = false) = apply {
    val declare = "int deinitStdTypes(JNIEnv *env) "
    if (!isImpl) {
        body { statement(declare) }
        return@apply
    }
    body {
        lines(1)
        line("$declare {")
        resetIndexStructure("booleanIndex")
        line("}")
    }
}


fun CodeBuilder.mapFromJava(isImpl: Boolean = false) = apply {
    body {
        mapBooleanFromJava(isImpl)
        mapFromJString(isImpl)
    }
}

fun CodeBuilder.mapToJava(isImpl: Boolean = false) = apply {
    body {
        mapBooleanToJava(isImpl)
        mapToJString(isImpl)
    }
}


private fun Poet.stdTypeIndexStructure(name: String) {
    line("struct ${name.firstCamelCase()} {")
    statement("jclass cls")
    statement("\tjmethodID toJvm = NULL")
    statement("\tjmethodID fromJvm = NULL")
    statement("}")
    statement("static std::shared_ptr<BooleanIndex> $name = {}")
}

private fun Poet.resetIndexStructure(name: String) {
    statement("if (${name}) env->DeleteGlobalRef(${name}->cls)")
    statement("${name}.reset()")
}