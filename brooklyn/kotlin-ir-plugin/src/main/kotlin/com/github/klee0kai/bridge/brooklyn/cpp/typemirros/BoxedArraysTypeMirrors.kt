package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.kotlinFqName

internal fun boxedArraysTypeMirrors() = arrayOf(
    // BOOLEAN
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Boolean;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<int>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isBoolean() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedBooleanArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedBooleanArray(env,  std::make_shared<std::vector<int>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Boolean;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isBoolean() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedBooleanArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedBooleanArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Boolean;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<std::shared_ptr<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Boolean",
                    "kotlin.Boolean",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBooleanNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBooleanNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<int>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Boolean;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<int>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Boolean",
                    "kotlin.Boolean",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBooleanNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBooleanNullableArray(env, $variable ) " },
    ),


    // BYTE
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Byte;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<int>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isByte() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedByteArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedByteArray(env,  std::make_shared<std::vector<int>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Byte;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isByte() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedByteArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedByteArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Byte;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<std::shared_ptr<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Byte",
                    "kotlin.Byte",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJByteNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJByteNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<int>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Byte;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<int>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Byte",
                    "kotlin.Byte",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJByteNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJByteNullableArray(env, $variable ) " },
    ),


    //Char
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Character;",
        cppSimpleTypeMirrorStr = "char",
        cppFullTypeMirror = "std::vector<char>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isChar() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedCharArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedCharArray(env,  std::make_shared<std::vector<char>>( $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Character;",
        cppSimpleTypeMirrorStr = "char",
        cppFullTypeMirror = "std::shared_ptr<std::vector<char>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isChar() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedCharArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedCharArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Character;",
        cppSimpleTypeMirrorStr = "char",
        cppFullTypeMirror = "std::vector<std::shared_ptr<char>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Character",
                    "kotlin.Char",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJCharNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJCharNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<char>>>(  $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Character;",
        cppSimpleTypeMirrorStr = "char",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<char>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Character",
                    "kotlin.Char",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJCharNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJCharNullableArray(env, $variable ) " },
    ),


    //Short
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Short;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<int>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isShort() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedShortArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedShortArray(env,  std::make_shared<std::vector<int>>( $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Short;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isShort() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedShortArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedShortArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Short;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<std::shared_ptr<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Short",
                    "kotlin.Short",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJShortNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJShortNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<int>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Short;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<int>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Short",
                    "kotlin.Short",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJShortNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJShortNullableArray(env, $variable ) " },
    ),


    //Integer
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Integer;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<int>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isInt() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedIntegerArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedIntegerArray(env,  std::make_shared<std::vector<int>>( $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Integer;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isInt() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedIntegerArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedIntegerArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Integer;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::vector<std::shared_ptr<int>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Integer",
                    "kotlin.Int",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJIntegerNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJIntegerNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<int>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Integer;",
        cppSimpleTypeMirrorStr = "int",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<int>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Integer",
                    "kotlin.Int",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJIntegerNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJIntegerNullableArray(env, $variable ) " },
    ),

    //Long
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Long;",
        cppSimpleTypeMirrorStr = "int64_t",
        cppFullTypeMirror = "std::vector<int64_t>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isLong() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedLongArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedLongArray(env,  std::make_shared<std::vector<int64_t>>( $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Long;",
        cppSimpleTypeMirrorStr = "int64_t",
        cppFullTypeMirror = "std::shared_ptr<std::vector<int64_t>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isLong() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedLongArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedLongArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Long;",
        cppSimpleTypeMirrorStr = "int64_t",
        cppFullTypeMirror = "std::vector<std::shared_ptr<int64_t>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Long",
                    "kotlin.Long",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJLongNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJLongNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<int64_t>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Long;",
        cppSimpleTypeMirrorStr = "int64_t",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<int64_t>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Long",
                    "kotlin.Long",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJLongNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJLongNullableArray(env, $variable ) " },
    ),


    //Float
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Float;",
        cppSimpleTypeMirrorStr = "float",
        cppFullTypeMirror = "std::vector<float>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isFloat() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedFloatArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedFloatArray(env,  std::make_shared<std::vector<float>>( $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Float;",
        cppSimpleTypeMirrorStr = "float",
        cppFullTypeMirror = "std::shared_ptr<std::vector<float>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isFloat() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedFloatArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedFloatArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Float;",
        cppSimpleTypeMirrorStr = "float",
        cppFullTypeMirror = "std::vector<std::shared_ptr<float>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Float",
                    "kotlin.Float",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJFloatNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJFloatNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<float>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Float;",
        cppSimpleTypeMirrorStr = "float",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<float>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Float",
                    "kotlin.Float",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJFloatNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJFloatNullableArray(env, $variable ) " },
    ),


    //Double
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Double;",
        cppSimpleTypeMirrorStr = "double",
        cppFullTypeMirror = "std::vector<double>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isArray { it.isDouble() } },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJBoxedDoubleArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedDoubleArray(env,  std::make_shared<std::vector<double>>( $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Double;",
        cppSimpleTypeMirrorStr = "double",
        cppFullTypeMirror = "std::shared_ptr<std::vector<double>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type -> type.isNullableArray { it.isDouble() } },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJBoxedDoubleArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJBoxedDoubleArray(env, $variable ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Double;",
        cppSimpleTypeMirrorStr = "double",
        cppFullTypeMirror = "std::vector<std::shared_ptr<double>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Double",
                    "kotlin.Double",
                )
            }
        },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJDoubleNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJDoubleNullableArray(env,  std::make_shared<std::vector<std::shared_ptr<double>>>(   $variable ) ) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/Double;",
        cppSimpleTypeMirrorStr = "double",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<double>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.Double",
                    "kotlin.Double",
                )
            }
        },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJDoubleNullableArray(env, ${variable}) " },
        transformToJni = { variable -> "brooklyn::mapper::mapToJDoubleNullableArray(env, $variable ) " },
    ),

    )