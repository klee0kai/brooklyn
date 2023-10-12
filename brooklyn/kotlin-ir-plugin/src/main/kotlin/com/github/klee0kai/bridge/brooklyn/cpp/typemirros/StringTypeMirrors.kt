package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import org.jetbrains.kotlin.ir.util.kotlinFqName

internal fun stringTypeMirror() =
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/String;",
        cppTypeMirrorStr = "std::string",
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
    )

internal fun stringNullableTypeMirror() =
    CppTypeMirror(
        jniTypeCode = "Ljava/lang/String;",
        cppTypeMirrorStr = "std::shared_ptr<std::string>",
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
    )


fun extractJniString(method: String) = ExtractJniType { jvmObj, fieldOrMethodId, args ->
    " ( jstring )  env->${method}( ${listOf(jvmObj, fieldOrMethodId, args).joinArgs()} )"
}

