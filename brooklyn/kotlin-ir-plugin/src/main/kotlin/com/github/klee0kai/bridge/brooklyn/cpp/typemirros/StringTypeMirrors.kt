package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import com.github.klee0kai.bridge.brooklyn.cpp.common.isArray
import com.github.klee0kai.bridge.brooklyn.cpp.common.isNullableArray
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.util.kotlinFqName

internal fun stringsTypeMirror() = arrayOf(
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/String;",
        cppSimpleTypeMirrorStr = "std::string",
        jniTypeStr = "jstring",
        checkIrClass = { it, nullable ->
            !nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.String",
                "kotlin.String",
                "kotlin.CharSequence",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJString(env, std::make_shared<std::string>( $variable ) )" },
        transformToCpp = { variable -> "*brooklyn::mapper::mapJString(env, ${variable}) " },
        extractFromField = extractJniString("GetObjectField"),
        extractFromStaticField = extractJniString("GetStaticObjectField"),
        extractFromMethod = extractJniString("CallObjectMethod"),
        extractFromStaticMethod = extractJniString("CallStaticObjectMethod"),
    ),
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/String;",
        cppSimpleTypeMirrorStr = "std::shared_ptr<std::string>",
        jniTypeStr = "jstring",
        checkIrClass = { it, _ ->
            it.kotlinFqName.toString() in listOf(
                "java.lang.String",
                "kotlin.String",
                "kotlin.CharSequence",
            )
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJString(env, $variable ) " },
        transformToCpp = { variable -> "brooklyn::mapper::mapJString(env, ${variable}) " },
        extractFromField = extractJniString("GetObjectField"),
        extractFromStaticField = extractJniString("GetStaticObjectField"),
        extractFromMethod = extractJniString("CallObjectMethod"),
        extractFromStaticMethod = extractJniString("CallStaticObjectMethod"),
        insertToField = insertJniType("SetObjectField"),
        insertToStaticField = insertJniType("SetStaticObjectField"),
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/String;",
        cppSimpleTypeMirrorStr = "std::string",
        cppFullTypeMirror = "std::vector<std::string>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                !argType.isNullable() && argClass.kotlinFqName.toString() in listOf(
                    "java.lang.String",
                    "kotlin.String",
                    "kotlin.CharSequence",
                )
            }
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJStringArray(env,  std::make_shared<std::vector<std::string>>(   $variable ) ) " },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJStringArray(env, ${variable}) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/String;",
        cppSimpleTypeMirrorStr = "std::string",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::string>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                !argType.isNullable() && argClass.kotlinFqName.toString() in listOf(
                    "java.lang.String",
                    "kotlin.String",
                    "kotlin.CharSequence",
                )
            }
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJStringArray(env, $variable )  " },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJStringArray(env, ${variable}) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/String;",
        cppSimpleTypeMirrorStr = "std::string",
        cppFullTypeMirror = "std::vector<std::shared_ptr<std::string>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isArray { argType ->
                val argClass = argType.getClass() ?: return@isArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.String",
                    "kotlin.String",
                    "kotlin.CharSequence",
                )
            }
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJStringArrayNullable(env,  std::make_shared<std::vector<std::shared_ptr<std::string>>>(   $variable ) ) " },
        transformToCpp = { variable -> "*brooklyn::mapper::mapFromJStringArrayNullable(env, ${variable}) " },
    ),
    CppTypeMirror(
        jniTypeCode = "[Ljava/lang/String;",
        cppSimpleTypeMirrorStr = "std::string",
        cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<std::string>>>",
        jniTypeStr = "jobjectArray",
        checkIrType = { type ->
            type.isNullableArray { argType ->
                val argClass = argType.getClass() ?: return@isNullableArray false
                argClass.kotlinFqName.toString() in listOf(
                    "java.lang.String",
                    "kotlin.String",
                    "kotlin.CharSequence",
                )
            }
        },
        transformToJni = { variable -> "brooklyn::mapper::mapToJStringArrayNullable(env, $variable )  " },
        transformToCpp = { variable -> "brooklyn::mapper::mapFromJStringArrayNullable(env, ${variable}) " },
    ),
)


fun extractJniString(method: String) = ExtractJniType { type, jvmObj, fieldOrMethodId, args ->
    " ( ${type.jniTypeStr} )  env->${method}( ${listOf(jvmObj, fieldOrMethodId, args).joinArgs()} )"
}


