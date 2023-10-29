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
        transformToJni = { variable -> "brooklyn::mapper::mapToJBooleanBoxed(env,  $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBooleanBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJIntegerBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJIntegerBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJLongBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJLongBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJFloatBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJFloatBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJDoubleBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJDoubleBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJCharBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJCharBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJShortBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJShortBoxed(env, $variable ) " },
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
        transformToJni = { variable -> "brooklyn::mapper::mapToJByteBoxed(env, $variable )" },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJByteBoxed(env, $variable ) " },
    ),

    )