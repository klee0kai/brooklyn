package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.types.isNullableString
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.kotlinFqName

internal fun stringTypeMirror() =
    CppTypeMirror(
        jniTypeStr = "jstring",
        jniTypeCode = "Ljava/lang/String;",
        cppTypeMirrorStr = "std::string",
        checkIrType = { it.isString() },
        checkIrClass = { it, nullable ->
            !nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.String",
                "kotlin.String"
            )
        },
        mapFromJvmField = stringGetMethodCall("GetObjectField"),
        mapFromJvmStaticField = stringGetMethodCall("GetStaticObjectField"),
        mapFromJvmGetMethod = stringGetMethodCall("CallObjectMethod"),
        mapFromJvmGetStaticMethod = stringGetMethodCall("CallStaticObjectMethod"),
        mapToJvmField = simpleTodo,
        mapToJvmSetMethod = simpleTodo,
    )

internal fun stringNullableTypeMirror() =
    CppTypeMirror(
        jniTypeStr = "jstring",
        jniTypeCode = "Ljava/lang/String;",
        cppTypeMirrorStr = "std::shared_ptr<std::string>",
        checkIrType = { it.isNullableString() },
        checkIrClass = { it, nullable ->
            it.kotlinFqName.toString() in listOf(
                "java.lang.String",
                "kotlin.String"
            )
        },
        mapFromJvmField = stringGetMethodCall("GetObjectField"),
        mapFromJvmStaticField = stringGetMethodCall("GetStaticObjectField"),
        mapFromJvmGetMethod = stringGetMethodCall("CallObjectMethod"),
        mapFromJvmGetStaticMethod = stringGetMethodCall("CallStaticObjectMethod"),
        mapToJvmField = simpleTodo,
        mapToJvmSetMethod = simpleTodo,
    )


private fun stringGetMethodCall(name: String): MapJvmVariable {
    return MapJvmVariable { variable: String, env: String, jvmObj: String, methodId: String ->
        Poet().apply {
            val jFieldName = "jField$unicFieldIndex"
            val cppFieldName = "cppField${unicFieldIndex++}"
            statement("jstring $jFieldName = ( jstring ) ${env}->${name}($jvmObj, $methodId)")
            statement("const char *${cppFieldName} =  $jFieldName != NULL ? env->GetStringUTFChars( ${jFieldName}, NULL) : NULL")

            statement("$variable = $cppFieldName ?: \"\" ")
            statement(" if ( $jFieldName != NULL) env->ReleaseStringUTFChars( ${jFieldName}, ${cppFieldName})")
        }
    }
}
