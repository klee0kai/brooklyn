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
        stdTypeIndexStructure("integerIndex")
        stdTypeIndexStructure("longIndex")
    }

    body {
        lines(1)
        line("${declare}{")
        booleanIndexInit()
        integerIndexInit()
        longIndexInit()
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


fun CodeBuilder.stdTypeMappers(isImpl: Boolean = false) = apply {
    body {
        mapBooleanFromJava(isImpl)
        mapBooleanToJava(isImpl)

        mapIntegerFromJava(isImpl)
        mapIntegerToJava(isImpl)

        mapLongFromJava(isImpl)
        mapLongToJava(isImpl)

        mapFromJString(isImpl)
        mapToJString(isImpl)
    }
}


private fun Poet.stdTypeIndexStructure(name: String) {
    line("struct ${name.firstCamelCase()} {")
    statement("jclass cls")
    statement("\tjmethodID toJvm = NULL")
    statement("\tjmethodID fromJvm = NULL")
    statement("}")
    statement("static std::shared_ptr<${name.firstCamelCase()}> $name = {}")
}

private fun Poet.resetIndexStructure(name: String) {
    statement("if (${name}) env->DeleteGlobalRef(${name}->cls)")
    statement("${name}.reset()")
}