package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.codegen.BrooklynIrGenerationExtension.Companion.pluginContextRef
import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId

fun CodeBuilder.mapJniClassApi(jClass: IrClass) = apply {
    body.post(Poet().apply {
        val clId = jClass.classId!!
        val context = pluginContextRef?.get()!!

        lines(1)
        statement("int mapFromJvm(JNIEnv *env)")
    })
}

fun CodeBuilder.mapJniClassImpl(jClass: IrClass) = apply {
}