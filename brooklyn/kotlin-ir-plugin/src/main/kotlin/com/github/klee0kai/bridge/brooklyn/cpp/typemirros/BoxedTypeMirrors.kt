package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import org.jetbrains.kotlin.ir.util.kotlinFqName

internal fun boxedTypeMirrors() = arrayOf(
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Boolean;",
        cppTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Boolean",
                "kotlin.Boolean",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoolean(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoolean(env,  $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Integer;",
        cppTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Integer",
                "kotlin.Int",
                "kotlin.Number",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJInteger(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJInteger(env, $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Long;",
        cppTypeMirrorStr = "std::shared_ptr<int64_t>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Long",
                "kotlin.Long",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJLong(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJLong(env, $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Float;",
        cppTypeMirrorStr = "std::shared_ptr<float>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Float",
                "kotlin.Float",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJFloat(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJFloat(env, $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Double;",
        cppTypeMirrorStr = "std::shared_ptr<double>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Double",
                "kotlin.Double",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJDouble(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJDouble(env, $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Character;",
        cppTypeMirrorStr = "std::shared_ptr<char>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Character",
                "kotlin.Char",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJChar(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJChar(env, $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Short;",
        cppTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Short",
                "kotlin.Short",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJShort(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJShort(env, $variable )" },
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/Byte;",
        cppTypeMirrorStr = "std::shared_ptr<int>",
        checkIrClass = { it, nullable ->
            nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.Byte",
                "kotlin.Byte",
            )
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJByte(env, $variable ) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJByte(env, $variable )" },
    ),

    )