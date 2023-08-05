package com.github.klee0kai.bridge.brooklyn.cpp

import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.ClassId
import kotlin.math.absoluteValue

fun CodeBuilder.declareClassIndexStructure(jClass: IrClass) = apply {
    variables.post(Poet().apply {
        val clId = jClass.classId!!
        lines(1)
        line("static struct ${clId.indexStructName} {")
        jClass.fields.forEach { field ->
            statement("\tjfieldID ${field.name} = NULL")
        }
        jClass.properties.forEach { property ->
            statement("\tjmethodID ${property.name}_getter = NULL")
            if (property.isVar) statement("\tjmethodID ${property.name}_setter = NULL")
        }
        (jClass.constructors + jClass.functions).forEach { func ->
            statement("\tjmethodID ${func.cppNameMirror} = NULL")
        }
        statement("} *${clId.indexVariableName} = NULL")
    })
}

fun CodeBuilder.initJniClassApi(jClass: IrClass) = apply {
    body.post(Poet().apply {
        val clId = jClass.classId!!
        lines(1)
        statement("int ${clId.initIndexFuncName}(JNIEnv *env)")
    })
}

fun CodeBuilder.initJniClassImpl(jClass: IrClass) = apply {
    body.post(Poet().apply {
        lines(1)
        val clId = jClass.classId!!
        val clPathName = "${clId.packageFqName}/${clId.shortClassName}".snakeCase("/")
        line("int ${clId.initIndexFuncName}(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) return 0")
        statement("${clId.indexVariableName} = new ${clId.indexStructName} {}")
        statement("jclass cls = env->FindClass(\"$clPathName\")")
        jClass.fields.forEach { field ->
            post("${clId.indexVariableName}->${field.name} = env->GetFieldID(cls, ")
            str(field.name.toString())
            post(",")
            str(field.type.cppTypeMirror)
            statement(")")
            statement("if(!${clId.indexVariableName}->${field.name}) return -1")
        }
        jClass.properties.forEach { property ->
            val type = property.getter!!.returnType
            //getter
            post("${clId.indexVariableName}->${property.name}_getter = env->GetMethodID(cls, ")
            str("get${property.name.toString().firstCamelCase()}")
            post(", ")
            str("()${type.cppTypeMirror}")
            statement(")")
            statement("if(!${clId.indexVariableName}->${property.name}_getter) return -1")
            //setter
            post("${clId.indexVariableName}->${property.name}_setter = env->GetMethodID(cls, ")
            str("set${property.name.toString().firstCamelCase()}")
            post(",")
            post("\"(${type.cppTypeMirror})V\"")
            statement(")")
            statement("if(!${clId.indexVariableName}->${property.name}_setter) return -1")
        }
        (jClass.constructors + jClass.functions).forEach { func ->
            post("${clId.indexVariableName}->${func.cppNameMirror} = env->GetMethodID(cls, ")
            str(func.name.toString())
            post(", ")
            val argTypes = func.fullValueParameterList.joinToString("") { it.type.cppTypeMirror }
            val returnType = if (func.isConstructor) "V" else func.returnType.cppTypeMirror
            post("\"(${argTypes})${returnType}\"")
            statement(")")
            statement("if(!${clId.indexVariableName}->${func.cppNameMirror}) return -1")
        }

        statement("return 0")
        line("}")
    })
}

fun CodeBuilder.deinitJniClassApi(jClass: IrClass) = apply {
    body.post(Poet().apply {
        val clId = jClass.classId!!
        lines(1)
        statement("int ${clId.deinitIndexFuncName}()")
    })
}


fun CodeBuilder.deinitJniClassImpl(jClass: IrClass) = apply {
    body.post(Poet().apply {
        val clId = jClass.classId!!
        lines(1)
        line("int ${clId.deinitIndexFuncName}() {")
        statement("if (${clId.indexVariableName}) delete ${clId.indexVariableName}")
        statement("${clId.indexVariableName} = NULL")
        statement("return 0")
        line("}")
    })
}


val ClassId.initIndexFuncName
    get() = "init_${packageFqName}${shortClassName}".camelCase()

val ClassId.deinitIndexFuncName
    get() = "deinit_${packageFqName}${shortClassName}".camelCase()


val ClassId.indexStructName
    get() = "${packageFqName}${shortClassName}IndexStructure".camelCase().firstCamelCase()

val ClassId.indexVariableName
    get() = "${packageFqName}${shortClassName}Index".camelCase()

val IrFunction.cppNameMirror
    get() = "$name${fullValueParameterList.map { it.type to it.isVararg }.hashCode().absoluteValue}".camelCase()

val IrFunction.isConstructor
    get() = name.toString() == "<init>"


// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html
val IrType.cppTypeMirror
    get() = when {
        isBoolean() -> "Z"
        isByte() -> "B"
        isChar() -> "C"
        isShort() -> "S"
        isInt() -> "I"
        isLong() -> "J"
        isFloat() -> "F"
        isDouble() -> "D"
        isString() || isNullableString() -> "Ljava/lang/String;"
        isAny() || isNullableAny() -> "Ljava/lang/Object;"
        isArray() || isNullableArray() -> "[" + ""
        else -> {
            "L${classFqName.toString().snakeCase("/")};"
        }

    }

