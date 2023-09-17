package com.github.klee0kai.bridge.brooklyn.cpp.model

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.mapper.isClassType
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.fields
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.declareClassModelStructure(jClass: IrClass) = apply {
    variables {

        lines(1)
        line("struct ${jClass.cppTypeMirror()} {")
        jClass.fields.forEach { field ->
            field.type.cppTypeMirror()?.let { cppType ->
                statement("\t${cppType} ${field.name};")
            }
        }
        jClass.properties.forEach { property ->
            val type = property.getter!!.returnType
            type.cppTypeMirror()?.let { cppType ->
                statement("\t${cppType} ${property.name}")
            }
        }
        line("};")

    }
}


@Deprecated("use findJniTypeMirror")
fun IrType.cppTypeMirror() = when {
    isBoolean() -> "int"
    isByte() -> "int"
    isChar() -> "char"
    isShort() -> "int"
    isInt() -> "int"
    isLong() -> "long long"
    isFloat() -> "float"
    isDouble() -> "double"
    isClassType(IdSignatureValues._boolean) -> "std::shared_ptr<int>"
    isClassType(IdSignatureValues._char) -> "std::shared_ptr<char>"
    isClassType(IdSignatureValues._byte) -> "std::shared_ptr<int>"
    isClassType(IdSignatureValues._short) -> "std::shared_ptr<int>"
    isClassType(IdSignatureValues._int) -> "std::shared_ptr<int>"
    isClassType(IdSignatureValues._long) -> "std::shared_ptr<long long>"
    isClassType(IdSignatureValues._float) -> "std::shared_ptr<float>"
    isClassType(IdSignatureValues._double) -> "std::shared_ptr<double>"
    isClassType(IdSignatureValues.number) -> "std::shared_ptr<long long>"
    isClassType(IdSignatureValues.charSequence) -> "std::shared_ptr<std::string>"
    else -> getClass()?.cppTypeMirror(nullable = isNullable())

}

@Deprecated("use findJniTypeMirror")
fun IrClass.cppTypeMirror(nullable: Boolean = false) = when (kotlinFqName.toString()) {
    "java.lang.String" -> "std::shared_ptr<std::string>"
    "kotlin.String" -> "std::shared_ptr<std::string>"
    "kotlin.Unit" -> null
    else -> {
        "${classId!!.packageFqName}${classId!!.shortClassName}".camelCase().firstCamelCase()
    }
}

fun IrClass.cppMappingNameSpace() = "${cppTypeMirror()}_mapping"
