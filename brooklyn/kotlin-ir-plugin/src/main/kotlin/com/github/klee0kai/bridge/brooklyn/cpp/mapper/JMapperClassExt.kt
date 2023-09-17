package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.lines
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.cpp.model.cppMappingNameSpace
import com.github.klee0kai.bridge.brooklyn.cpp.model.cppTypeMirror
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId

fun CodeBuilder.mapJniClassApi(jClass: IrClass) = apply {
    body.post(Poet().apply {
        val typeMirror = jClass.cppTypeMirror()
        val nameSpace = jClass.cppMappingNameSpace()

        lines(1)
        line("namespace $nameSpace {")

        lines(1)
        statement("std::shared_ptr<$typeMirror> mapFromJvm(JNIEnv *env, jobject j$typeMirror)")

        lines(1)
        statement("jobject mapToJvm(JNIEnv *env, std::shared_ptr<$typeMirror> j$typeMirror)")

        lines(1)
        statement("std::vector<$typeMirror> mapArrayFromJvm(JNIEnv *env, jobjectArray j$typeMirror)")

        lines(1)
        statement("jobjectArray mapArrayToJvm(JNIEnv *env, std::vector<$typeMirror> j$typeMirror)")

        lines(1)
        line("}// namespace $nameSpace")
    })
}

fun CodeBuilder.mapJniClassImpl(jClass: IrClass) = apply {
    body.post(Poet().apply {
        val typeMirror = jClass.cppTypeMirror()
        val nameSpace = jClass.cppMappingNameSpace()

        lines(1)
        line("namespace $nameSpace {")

        mapFromJvmImpl(jClass)



        lines(1)
        line("}// namespace $nameSpace")
    })
}

private fun Poet.mapFromJvmImpl(jClass: IrClass) = apply {
    val jvmObjectName = "jvmObject"
    val cppObjectName = "cppObject"
    val indexClVariable = jClass.classId!!.indexVariableName
    val typeMirror = jClass.cppTypeMirror()

    lines(1)
    line("std::shared_ptr<$typeMirror> mapFromJvm(JNIEnv *env, jobject $jvmObjectName )")
    line("{")
    line("std::shared_ptr<$typeMirror> $cppObjectName = std::make_shared<$typeMirror>();")



    statement("return $cppObjectName")
    line("}")
    lines(1)
}


