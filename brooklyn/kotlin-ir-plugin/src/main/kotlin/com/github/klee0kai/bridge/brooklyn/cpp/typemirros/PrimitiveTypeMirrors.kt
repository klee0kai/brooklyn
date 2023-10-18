package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import org.jetbrains.kotlin.ir.types.*

internal fun primitiveTypeMirrors() = arrayOf(
    CppTypeMirror(
        jniTypeCode = "Z",
        cppSimpleTypeMirrorStr = "int",
        jniTypeStr = "jboolean",
        checkIrType = { it.isBoolean() },
        transformToJni = { variable -> "$variable ? 1 : 0 " },
        transformToCpp = castType("int"),
        extractFromField = extractJniType("GetBooleanField"),
        extractFromStaticField = extractJniType("GetStaticBooleanField"),
        extractFromMethod = extractJniType("CallBooleanMethod"),
        extractFromStaticMethod = extractJniType("CallStaticBooleanMethod"),
        insertToField = insertJniType("SetBooleanField"),
        insertToStaticField = insertJniType("SetStaticBooleanField"),
    ),
    CppTypeMirror(
        jniTypeCode = "B",
        cppSimpleTypeMirrorStr = "int",
        jniTypeStr = "jbyte",
        checkIrType = { it.isByte() },
        transformToJni = castType("jbyte"),
        transformToCpp = castType("int"),
        extractFromField = extractJniType("GetByteField"),
        extractFromStaticField = extractJniType("GetStaticByteField"),
        extractFromMethod = extractJniType("CallByteMethod"),
        extractFromStaticMethod = extractJniType("CallStaticByteMethod"),
        insertToField = insertJniType("SetByteField"),
        insertToStaticField = insertJniType("SetStaticByteField"),
    ),
    CppTypeMirror(
        jniTypeCode = "C",
        cppSimpleTypeMirrorStr = "char",
        jniTypeStr = "jchar",
        checkIrType = { it.isChar() },
        transformToJni = castType("jchar"),
        transformToCpp = castType("char"),
        extractFromField = extractJniType("GetCharField"),
        extractFromStaticField = extractJniType("GetStaticCharField"),
        extractFromMethod = extractJniType("CallCharMethod"),
        extractFromStaticMethod = extractJniType("CallStaticCharMethod"),
        insertToField = insertJniType("SetCharField"),
        insertToStaticField = insertJniType("SetStaticCharField"),
    ),
    CppTypeMirror(
        jniTypeCode = "S",
        cppSimpleTypeMirrorStr = "int",
        jniTypeStr = "jshort",
        checkIrType = { it.isShort() },
        transformToJni = castType("jshort"),
        transformToCpp = castType("int"),
        extractFromField = extractJniType("GetShortField"),
        extractFromStaticField = extractJniType("GetStaticShortField"),
        extractFromMethod = extractJniType("CallShortMethod"),
        extractFromStaticMethod = extractJniType("CallStaticShortMethod"),
        insertToField = insertJniType("SetShortField"),
        insertToStaticField = insertJniType("SetStaticShortField"),
    ),
    CppTypeMirror(
        jniTypeCode = "I",
        cppSimpleTypeMirrorStr = "int",
        jniTypeStr = "jint",
        checkIrType = { it.isInt() },
        transformToJni = castType("jint"),
        transformToCpp = castType("int"),
        extractFromField = extractJniType("GetIntField"),
        extractFromStaticField = extractJniType("GetStaticIntField"),
        extractFromMethod = extractJniType("CallIntMethod"),
        extractFromStaticMethod = extractJniType("CallStaticIntMethod"),
        insertToField = insertJniType("SetIntField"),
        insertToStaticField = insertJniType("SetStaticIntField"),
    ),
    CppTypeMirror(
        jniTypeCode = "J",
        cppSimpleTypeMirrorStr = "int64_t",
        jniTypeStr = "jlong",
        checkIrType = { it.isLong() },
        transformToJni = castType("jlong"),
        transformToCpp = castType("int64_t"),
        extractFromField = extractJniType("GetLongField"),
        extractFromStaticField = extractJniType("GetStaticLongField"),
        extractFromMethod = extractJniType("CallLongMethod"),
        extractFromStaticMethod = extractJniType("CallStaticLongMethod"),
        insertToField = insertJniType("SetLongField"),
        insertToStaticField = insertJniType("SetStaticLongField"),

        ),
    CppTypeMirror(
        jniTypeCode = "F",
        cppSimpleTypeMirrorStr = "float",
        jniTypeStr = "jfloat",
        checkIrType = { it.isFloat() },
        transformToJni = castType("jfloat"),
        transformToCpp = castType("float"),
        extractFromField = extractJniType("GetFloatField"),
        extractFromStaticField = extractJniType("GetStaticFloatField"),
        extractFromMethod = extractJniType("CallFloatMethod"),
        extractFromStaticMethod = extractJniType("CallStaticFloatMethod"),
        insertToField = insertJniType("SetFloatField"),
        insertToStaticField = insertJniType("SetStaticFloatField"),

        ),
    CppTypeMirror(
        jniTypeCode = "D",
        cppSimpleTypeMirrorStr = "double",
        jniTypeStr = "jdouble",
        checkIrType = { it.isDouble() },
        transformToJni = castType("jdouble"),
        transformToCpp = castType("double"),
        extractFromField = extractJniType("GetDoubleField"),
        extractFromStaticField = extractJniType("GetStaticDoubleField"),
        extractFromMethod = extractJniType("CallDoubleMethod"),
        extractFromStaticMethod = extractJniType("CallStaticDoubleMethod"),
        insertToField = insertJniType("SetDoubleField"),
        insertToStaticField = insertJniType("SetStaticDoubleField"),

        ),


    )

fun castType(type: String) = TransformJniType { variable ->
    "$type($variable)"
}



