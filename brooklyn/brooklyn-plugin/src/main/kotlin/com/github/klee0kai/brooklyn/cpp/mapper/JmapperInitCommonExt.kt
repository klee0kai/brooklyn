package com.github.klee0kai.brooklyn.cpp.mapper

import com.github.klee0kai.brooklyn.cpp.common.*
import com.github.klee0kai.brooklyn.model.SupportClassMirror


fun CodeBuilder.initAllApi() = apply {
    body {
        statement("int init(JNIEnv *env)")
    }
}


fun CodeBuilder.initAllImpl(
    classMirrors: List<SupportClassMirror>
) = apply {
    header {
        classMirrors.forEach { irClass ->
            include(irClass.classId.mapperHeaderFile.path)
        }
    }

    body {
        line("int init(JNIEnv *env) {")
        statement("int initResult = 0")
        statement("initResult = initStdTypes(env)")
        statement("if (initResult) return initResult")
        classMirrors.forEach { clMirror ->
            statement("initResult = ${clMirror.namespace}::init(env)")
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


fun CodeBuilder.deinitAllImpl(classMirrors: List<SupportClassMirror>) = apply {
    body {
        line("int deinit(JNIEnv *env) {")
        statement("int initResult = 0")
        statement("initResult = deinitStdTypes(env)")
        statement("if (initResult) return initResult")
        classMirrors.forEach { clMirror ->
            statement("initResult = ${clMirror.namespace}::deinit(env)")
            statement("if (initResult) return initResult")
        }
        statement("return initResult")
        line("}")
    }
}

