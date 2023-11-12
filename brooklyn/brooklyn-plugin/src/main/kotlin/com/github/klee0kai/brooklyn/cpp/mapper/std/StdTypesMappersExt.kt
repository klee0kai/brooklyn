package com.github.klee0kai.brooklyn.cpp.mapper.std

import com.github.klee0kai.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.brooklyn.cpp.common.line
import com.github.klee0kai.brooklyn.cpp.common.lines
import com.github.klee0kai.brooklyn.cpp.common.statement
import com.github.klee0kai.brooklyn.poet.Poet

fun CodeBuilder.initStdTypes(isImpl: Boolean = false) = apply {
    val declare = "int initStdTypes(JNIEnv *env)"
    if (!isImpl) {
        body { statement(declare) }
        return@apply
    }
    header {
        statement("using namespace std")
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
        stdTypeIndexStructure("stringIndex")
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
        stringIndexInit()
        statement("return 0")
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
        resetIndexStructure("integerIndex")
        resetIndexStructure("longIndex")
        resetIndexStructure("floatIndex")
        resetIndexStructure("doubleIndex")
        resetIndexStructure("charIndex")
        resetIndexStructure("shortIndex")
        resetIndexStructure("byteIndex")
        statement("return 0")
        line("}")
    }
}


fun CodeBuilder.stdTypeMappers(isImpl: Boolean = false) = apply {
    body {
        mapBooleanFromJava(isImpl)
        mapBooleanToJava(isImpl)
        mapBooleanArrayFromJava(isImpl)
        mapBooleanArrayToJava(isImpl)

        mapIntegerFromJava(isImpl)
        mapIntegerToJava(isImpl)
        mapIntegerArrayFromJava(isImpl)
        mapIntegerArrayToJava(isImpl)

        mapLongFromJava(isImpl)
        mapLongToJava(isImpl)
        mapLongArrayFromJava(isImpl)
        mapLongArrayToJava(isImpl)

        mapFloatFromJava(isImpl)
        mapFloatToJava(isImpl)
        mapFloatArrayFromJava(isImpl)
        mapFloatArrayToJava(isImpl)

        mapDoubleFromJava(isImpl)
        mapDoubleToJava(isImpl)
        mapDoubleArrayFromJava(isImpl)
        mapDoubleArrayToJava(isImpl)

        mapCharFromJava(isImpl)
        mapCharToJava(isImpl)
        mapCharArrayFromJava(isImpl)
        mapCharArrayToJava(isImpl)

        mapShortFromJava(isImpl)
        mapShortToJava(isImpl)
        mapShortArrayFromJava(isImpl)
        mapShortArrayToJava(isImpl)

        mapByteFromJava(isImpl)
        mapByteToJava(isImpl)
        mapByteArrayFromJava(isImpl)
        mapByteArrayToJava(isImpl)

        mapFromJString(isImpl)
        mapToJString(isImpl)
        mapFromJStringArray(isImpl)
        mapFromJStringArrayNullable(isImpl)
        mapToJStringArray(isImpl)
        mapToJStringArrayNullable(isImpl)

    }
}


private fun Poet.stdTypeIndexStructure(name: String) {
    statement("static std::shared_ptr<SimpleIndexStructure> $name = {}")
}

private fun Poet.resetIndexStructure(name: String) {
    statement("if (${name}) env->DeleteGlobalRef(${name}->cls)")
    statement("${name}.reset()")
}