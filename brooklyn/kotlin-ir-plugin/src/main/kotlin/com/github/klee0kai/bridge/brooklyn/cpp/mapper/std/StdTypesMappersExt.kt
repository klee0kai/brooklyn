package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun CodeBuilder.initStdTypes(isImpl: Boolean = false) = apply {
    val declare = "int initStdTypes(JNIEnv *env)"
    if (!isImpl) {
        body { statement(declare) }
        return@apply
    }
    variables {
        line("struct SimpleIndexStructure {")
        statement("jclass cls")
        statement("\tjmethodID toJvm = NULL")
        statement("\tjmethodID fromJvm = NULL")
        statement("}")

        stdTypeIndexStructure("booleanIndex")
        stdTypeIndexStructure("integerIndex")
        stdTypeIndexStructure("longIndex")
        stdTypeIndexStructure("floatIndex")
        stdTypeIndexStructure("doubleIndex")
        stdTypeIndexStructure("charIndex")
        stdTypeIndexStructure("shortIndex")
        stdTypeIndexStructure("byteIndex")
    }

    body {
        lines(1)
        line("${declare}{")
        booleanIndexInit()
        integerIndexInit()
        longIndexInit()
        floatIndexInit()
        doubleIndexInit()
        charIndexInit()
        shortIndexInit()
        byteIndexInit()
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
//        resetIndexStructure("integerIndex")
//        resetIndexStructure("longIndex")
//        resetIndexStructure("floatIndex")
//        resetIndexStructure("doubleIndex")
//        resetIndexStructure("charIndex")
//        resetIndexStructure("shortIndex")
//        resetIndexStructure("byteIndex")
        statement("return 0")
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

        mapFloatFromJava(isImpl)
        mapFloatToJava(isImpl)

        mapDoubleFromJava(isImpl)
        mapDoubleToJava(isImpl)

        mapCharFromJava(isImpl)
        mapCharToJava(isImpl)

        mapShortFromJava(isImpl)
        mapShortToJava(isImpl)

        mapByteFromJava(isImpl)
        mapByteToJava(isImpl)

        mapFromJString(isImpl)
        mapToJString(isImpl)
    }
}


private fun Poet.stdTypeIndexStructure(name: String) {
    statement("static std::shared_ptr<SimpleIndexStructure> $name = {}")
}

private fun Poet.resetIndexStructure(name: String) {
    statement("if (${name}) env->DeleteGlobalRef(${name}->cls)")
    statement("${name}.reset()")
}