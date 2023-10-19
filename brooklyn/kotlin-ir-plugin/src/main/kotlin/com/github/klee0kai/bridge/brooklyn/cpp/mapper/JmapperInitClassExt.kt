package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isMarkedNullable
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import kotlin.math.absoluteValue

fun CodeBuilder.declareClassIndexStructure(jClass: IrClass) = apply {
    variables {
        val clId = jClass.classId!!
        lines(1)
        line("struct ${clId.indexStructName} {")
        statement("jclass cls")
        jClass.properties.forEach { property ->
            val annotations = property.annotations.isNotEmpty()
            statement("\tjmethodID ${property.name}_getter = NULL")
            if (property.isVar) statement("\tjmethodID ${property.name}_setter = NULL")
        }
        (jClass.constructors + jClass.functions).forEach { func ->
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

fun CodeBuilder.initJniClassImpl(jClass: IrClass) = apply {
    body {
        lines(1)
        val clId = jClass.classId!!
        val clPathName = "${clId.packageFqName}/${clId.shortClassName}".snakeCase("/")
        line("int init(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) return 0")
        statement("${clId.indexVariableName} = std::make_shared<${clId.indexStructName}>()")
        statement("${clId.indexVariableName}->cls = (jclass) env->NewGlobalRef( env->FindClass(\"$clPathName\") )")
        jClass.properties.forEach { property ->
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
        (jClass.constructors + jClass.functions).forEach { func ->
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


val ClassId.fullClassName
    get() = "${packageFqName}${shortClassName}"

val ClassId.indexStructName
    get() = "${fullClassName}IndexStructure".camelCase().firstUppercase()

val ClassId.indexVariableName
    get() = "${fullClassName}Index".camelCase()

val IrFunction.cppNameMirror
    get() = "$name${
        fullValueParameterList.map { it.type.classFqName to it.isVararg }.hashCode().absoluteValue
    }".camelCase()

val IrFunction.isConstructor
    get() = name.toString() == "<init>"

fun IrType.isClassType(signature: IdSignature.CommonSignature, nullable: Boolean? = null): Boolean {
    if (this !is IrSimpleType) return false
    if (nullable != null && this.isMarkedNullable() != nullable) return false
    return signature == classifier.signature ||
            classifier.owner.let { it is IrClass && it.hasFqNameEqualToSignature(signature) }
}

private fun IrClass.hasFqNameEqualToSignature(signature: IdSignature.CommonSignature): Boolean =
    name.asString() == signature.shortName &&
            hasEqualFqName(FqName("${signature.packageFqName}.${signature.declarationFqName}"))