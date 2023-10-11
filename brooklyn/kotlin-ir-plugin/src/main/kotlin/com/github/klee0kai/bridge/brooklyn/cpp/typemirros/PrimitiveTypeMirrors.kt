package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import org.jetbrains.kotlin.ir.types.*

internal fun primitiveTypeMirrors() = arrayOf(
    CppTypeMirror(
        jniTypeStr = "jboolean",
        jniTypeCode = "Z",
        cppTypeMirrorStr = "int",
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
        jniTypeStr = "jbyte",
        jniTypeCode = "B",
        cppTypeMirrorStr = "int",
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
        jniTypeStr = "jchar",
        jniTypeCode = "C",
        cppTypeMirrorStr = "char",
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
        jniTypeStr = "jshort",
        jniTypeCode = "S",
        cppTypeMirrorStr = "int",
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
        jniTypeStr = "jint",
        jniTypeCode = "I",
        cppTypeMirrorStr = "int",
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
        jniTypeStr = "jlong",
        jniTypeCode = "J",
        cppTypeMirrorStr = "int64_t",
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
        jniTypeStr = "jfloat",
        jniTypeCode = "F",
        cppTypeMirrorStr = "float",
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
        jniTypeStr = "jdouble",
        jniTypeCode = "D",
        cppTypeMirrorStr = "double",
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



