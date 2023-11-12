package com.github.klee0kai.brooklyn.cpp.mapper

import com.github.klee0kai.brooklyn.cpp.typemirros.cppMappingNameSpace
import com.github.klee0kai.brooklyn.cpp.common.*
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId


fun CodeBuilder.initAllApi() = apply {
    body {
        statement("int init(JNIEnv *env)")
    }
}


fun CodeBuilder.initAllImpl(
    classes: List<IrClass>
) = apply {
    header {
        classes.forEach { irClass ->
            include(irClass.classId!!.mapperHeaderFile.path)
        }
    }

    body {
        line("int init(JNIEnv *env) {")
        statement("int initResult = 0")
        statement("initResult = initStdTypes(env)")
        statement("if (initResult) return initResult")
        classes.forEach { clId ->
            statement("initResult = ${clId.cppMappingNameSpace()}::init(env)")
            statement("if (initResult) return initResult")
        }
        statement("return initResult")
        line("}")
    }
}

fun CodeBuilder.deinitAllApi() = apply {
    body {
        statement("int deinit(JNIEnv *env)")
    }
}


fun CodeBuilder.deinitAllImpl(classes: List<IrClass>) = apply {
    body {
        line("int deinit(JNIEnv *env) {")
        statement("int initResult = 0")
        statement("initResult = deinitStdTypes(env)")
        statement("if (initResult) return initResult")
        classes.forEach { irClass ->
            statement("initResult = ${irClass.cppMappingNameSpace()}::deinit(env)")
            statement("if (initResult) return initResult")
        }
        statement("return initResult")
        line("}")
    }
}

