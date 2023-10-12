package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppMappingNameSpace
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

fun CodeBuilder.initAllFromJvmApi() = apply {
    body {
        statement("int initJvm(JavaVM *pVM)")
    }
}


fun CodeBuilder.initAllFromJvmImpl() = apply {
    body {
        lines(1)
        line("int initJvm(JavaVM *pVM){")
        statement("JNIEnv *env = NULL")
        statement("pVM->GetEnv((void **) &env, JNI_VERSION_1_6)")
        statement("return init(env)")
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

