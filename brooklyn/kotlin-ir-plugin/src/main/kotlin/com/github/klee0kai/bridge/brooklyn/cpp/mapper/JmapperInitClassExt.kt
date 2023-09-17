package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
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
        statement("}")

        statement("static std::shared_ptr<${clId.indexStructName}> ${clId.indexVariableName} = {}")
    }
}

fun CodeBuilder.initJniClassApi(jClass: IrClass) = apply {
    body {
        val clId = jClass.classId!!
        lines(1)
        statement("int ${clId.initIndexFuncName}(JNIEnv *env)")
    }
}

fun CodeBuilder.initJniClassImpl(jClass: IrClass) = apply {
    body {
        lines(1)
        val clId = jClass.classId!!
        val clPathName = "${clId.packageFqName}/${clId.shortClassName}".snakeCase("/")
        line("int ${clId.initIndexFuncName}(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) return 0")
        statement("${clId.indexVariableName} = std::make_shared<${clId.indexStructName}>()")
        statement("jclass cls = env->FindClass(\"$clPathName\")")
        jClass.fields.forEach { field ->
            val jniTypeCode = field.type.jniType()?.jniTypeCode ?: return@forEach
            post("${clId.indexVariableName}->${field.name} = env->GetFieldID(cls, ")
            str(field.name.toString())
            post(",")
            str(jniTypeCode)
            statement(")")
            statement("if(!${clId.indexVariableName}->${field.name}) return -1")
        }
        jClass.properties.forEach { property ->
            val type = property.getter!!.returnType
            val jniTypeCode = type.jniType()?.jniTypeCode ?: return@forEach

            //getter
            post("${clId.indexVariableName}->${property.name}_getter = env->GetMethodID(cls, ")
            str("get${property.name.toString().firstCamelCase()}")
            post(", ")
            str("()${jniTypeCode}")
            statement(")")
            statement("if(!${clId.indexVariableName}->${property.name}_getter) return -1")
            //setter
            if (property.isVar) {
                post("${clId.indexVariableName}->${property.name}_setter = env->GetMethodID(cls, ")
                str("set${property.name.toString().firstCamelCase()}")
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
            }.getOrNull() ?: return@forEach


            post("${clId.indexVariableName}->${func.cppNameMirror} = env->GetMethodID(cls, ")
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

fun CodeBuilder.deinitJniClassApi(jClass: IrClass) = apply {
    body {
        val clId = jClass.classId!!
        lines(1)
        statement("int ${clId.deinitIndexFuncName}()")
    }
}


fun CodeBuilder.deinitJniClassImpl(jClass: IrClass) = apply {
    body {
        val clId = jClass.classId!!
        lines(1)
        line("int ${clId.deinitIndexFuncName}() {")
        statement("${clId.indexVariableName}.reset()")
        statement("return 0")
        line("}")
    }
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

fun IrType.isClassType(signature: IdSignature.CommonSignature, nullable: Boolean? = null): Boolean {
    if (this !is IrSimpleType) return false
    if (nullable != null && this.isMarkedNullable() != nullable) return false
    return signature == classifier.signature ||
            classifier.owner.let { it is IrClass && it.hasFqNameEqualToSignature(signature) }
}

private fun IrClass.hasFqNameEqualToSignature(signature: IdSignature.CommonSignature): Boolean =
    name.asString() == signature.shortName &&
            hasEqualFqName(FqName("${signature.packageFqName}.${signature.declarationFqName}"))