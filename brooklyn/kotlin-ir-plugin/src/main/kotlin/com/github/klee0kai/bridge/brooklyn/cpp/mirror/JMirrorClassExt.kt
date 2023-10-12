package com.github.klee0kai.bridge.brooklyn.cpp.mirror

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppModelMirror
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions

fun CodeBuilder.declareClassMirror(jClass: IrClass) = apply {
    val usedTypes = mutableSetOf<IrType>()

    body {
        val clMirror = jClass.cppModelMirror() ?: return@body
        lines(1)
        line("class $clMirror {")
        line("public: ")

        statement("${clMirror}(JNIEnv *env, jobject jvmSelf)")

        jClass.constructors.forEach { func ->
            val args = func.mirrorFuncArgs()?.joinToString(", ") ?: return@forEach
            statement("${clMirror}($args)")
        }

        jClass.functions.forEach { func ->
            val args = func.mirrorFuncArgs()?.joinToString(", ") ?: return@forEach
            val returnType = func.returnType.jniType()?.cppPtrTypeMirror ?: return@forEach
            statement("$returnType ${func.name}($args)")
        }

        statement("~${clMirror}()")

        line("private: ")
        statement("jobject jvmSelf")
        statement("}")
    }


    header {
        usedTypes.mapNotNull { type ->
            type.jniType()?.classId
        }.toSet().forEach { classId ->
            include(classId.modelHeaderFile.path)
        }
    }
}


fun IrFunction.mirrorFuncArgs() = runCatching {
    listOf("JNIEnv *env") + fullValueParameterList.map {
        "${it.type.jniType()!!.cppPtrTypeMirror} ${it.name}"
    }
}.getOrNull()