package com.github.klee0kai.bridge.brooklyn.cpp

import org.jetbrains.kotlin.name.ClassId


fun CodeBuilder.initAllApi() = apply {
    body {
        statement("int init(JNIEnv *env)")
    }
}


fun CodeBuilder.initAllImpl(
    classes: List<ClassId>
) = apply {
    header {
        classes.forEach { clId ->
            include(clId.mapperHeaderFile.path)
        }
    }

    body {
        line("int init(JNIEnv *env) {")
        statement("int initResult = 0")
        classes.forEach { clId ->
            statement("initResult = ${clId.initIndexFuncName}(env)")
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
        statement("int deinit()")
    }
}


fun CodeBuilder.deinitAllImpl(classes: List<ClassId>) = apply {
    body {
        line("int deinit() {")
        statement("int initResult = 0")
        classes.forEach { clId ->
            statement("initResult = ${clId.deinitIndexFuncName}()")
            statement("if (initResult) return initResult")
        }
        statement("return initResult")
        line("}")
    }
}

