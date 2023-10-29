package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.declareClassIndexStructure(jClass: IrClass, pojo: Boolean = false) = apply {
    variables {
        val clId = jClass.classId!!
        lines(1)
        line("struct ${clId.indexStructName} {")
        statement("jclass cls")
        jClass.properties.forEach { property ->
            if (property.isIgnoringJni) return@forEach
            statement("\tjmethodID ${property.name}_getter = NULL")
            if (property.isVar) statement("\tjmethodID ${property.name}_setter = NULL")
        }
        val methods = if (pojo) jClass.constructors else (jClass.constructors + jClass.functions)
        methods.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            statement("\tjmethodID ${func.cppNameMirror} = NULL")
        }
        statement("}")

        statement("extern std::shared_ptr<${clId.indexStructName}> ${clId.indexVariableName}")
    }
}

fun CodeBuilder.declareClassIndexField(jClass: IrClass) = apply {
    variables {
        val clId = jClass.classId!!
        statement("std::shared_ptr<${clId.indexStructName}> ${clId.indexVariableName} = {}")
    }
}


fun CodeBuilder.initJniClassApi() = apply {
    body {
        lines(1)
        statement("int init(JNIEnv *env)")
    }
}

fun CodeBuilder.initJniClassImpl(jClass: IrClass, pojo: Boolean = false) = apply {
    body {
        lines(1)
        val clId = jClass.classId!!
        val clPathName = "${clId.croppedPackageName}/${clId.shortClassName}".snakeCase("/")
        line("int init(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) return 0")
        statement("${clId.indexVariableName} = std::make_shared<${clId.indexStructName}>()")
        statement("${clId.indexVariableName}->cls = (jclass) env->NewGlobalRef( env->FindClass(\"$clPathName\") )")
        jClass.properties.forEach { property ->
            if (property.isIgnoringJni) return@forEach

            val type = property.getter!!.returnType
            val jniTypeCode = type.jniType()?.jniTypeCode ?: return@forEach

            //getter
            post("${clId.indexVariableName}->${property.name}_getter = env->GetMethodID(${clId.indexVariableName}->cls, ")
            str("get${property.nameUpperCase}")
            post(", ")
            str("()${jniTypeCode}")
            statement(")")
            statement("if(!${clId.indexVariableName}->${property.name}_getter) return -1")
            //setter
            if (property.isVar) {
                post("${clId.indexVariableName}->${property.name}_setter = env->GetMethodID(${clId.indexVariableName}->cls, ")
                str("set${property.nameUpperCase}")
                post(",")
                post("\"(${jniTypeCode})V\"")
                statement(")")
                statement("if(!${clId.indexVariableName}->${property.name}_setter) return -1")
            }
        }
        val methods = if (pojo) jClass.constructors else (jClass.constructors + jClass.functions)
        methods.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            val argTypes = runCatching {
                func.fullValueParameterList.joinToString("") { it.type.jniType()!!.jniTypeCode }
            }.getOrNull() ?: return@forEach
            val returnType = runCatching {
                if (func.isConstructor) "V" else func.returnType.jniType()!!.jniTypeCode
            }.getOrNull() ?: "V"


            post("${clId.indexVariableName}->${func.cppNameMirror} = env->GetMethodID(${clId.indexVariableName}->cls, ")
            str(func.name.toString())
            post(", ")
            post("\"(${argTypes})${returnType}\"")
            statement(")")
            statement("if(!${clId.indexVariableName}->${func.cppNameMirror}) return -1")
        }

        statement("return 0")
        line("}")
    }
}

fun CodeBuilder.deinitJniClassApi() = apply {
    body {
        lines(1)
        statement("int deinit(JNIEnv *env)")
    }
}


fun CodeBuilder.deinitJniClassImpl(jClass: IrClass) = apply {
    body {
        val clId = jClass.classId!!
        lines(1)
        line("int deinit(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) env->DeleteGlobalRef(${clId.indexVariableName}->cls)")
        statement("${clId.indexVariableName}.reset()")
        statement("return 0")
        line("}")
    }
}

