package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import org.jetbrains.kotlin.ir.util.kotlinFqName

internal fun boxedTypeMirrors() = arrayOf(
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Boolean;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Boolean",
                "kotlin.Boolean",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoolean(env,  $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoolean(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Integer;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Integer",
                "kotlin.Int",
                "kotlin.Number",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJInteger(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJInteger(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Long;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<int64_t>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Long",
                "kotlin.Long",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJLong(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJLong(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Float;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<float>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Float",
                "kotlin.Float",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJFloat(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJFloat(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Double;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<double>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Double",
                "kotlin.Double",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJDouble(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJDouble(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Character;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<char>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Character",
                "kotlin.Char",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJChar(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJChar(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Short;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Short",
                "kotlin.Short",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJShort(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJShort(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Byte;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Byte",
                "kotlin.Byte",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJByte(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJByte(env, $variable ) " },
    ),

    )