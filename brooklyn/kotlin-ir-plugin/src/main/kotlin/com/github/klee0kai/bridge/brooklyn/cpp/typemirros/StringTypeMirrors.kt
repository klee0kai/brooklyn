package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.util.kotlinFqName

private class JStringTransformMeta(
    val jniVariable: String,
    val tempCppVariable: String
)

internal fun stringTypeMirror() =
    CppTypeMirror(
        jniTypeStr = "jstring",
        jniTypeCode = "Ljava/lang/String;",
        cppTypeMirrorStr = "std::string",
        checkIrClass = { it, nullable ->
            !nullable && it.kotlinFqName.toString() in listOf(
                "java.lang.String",
                "kotlin.String"
            )
        },
        transformToJni = { variable -> "env->NewStringUTF(${variable}.c_str())" },
        extractFromField = extractJniString("GetObjectField"),
        extractFromStaticField = extractJniString("GetStaticObjectField"),
        extractFromMethod = extractJniString("CallObjectMethod"),
        extractFromStaticMethod = extractJniString("CallStaticObjectMethod"),
        insertToField = insertJniType("SetObjectField"),
        insertToStaticField = insertJniType("SetStaticObjectField"),
        transformToCppLong = jStringToCppStringTransform(ptr = false)
    )

internal fun stringNullableTypeMirror() =
    CppTypeMirror(
        jniTypeStr = "jstring",
        jniTypeCode = "Ljava/lang/String;",
        cppTypeMirrorStr = "std::shared_ptr<std::string>",
        checkIrClass = { it, _ ->
            it.kotlinFqName.toString() in listOf(
                "java.lang.String",
                "kotlin.String"
            )
        },
        transformToJni = { variable -> "env->NewStringUTF(${variable}->c_str())" },
        extractFromField = extractJniString("GetObjectField"),
        extractFromStaticField = extractJniString("GetStaticObjectField"),
        extractFromMethod = extractJniString("CallObjectMethod"),
        extractFromStaticMethod = extractJniString("CallStaticObjectMethod"),
        insertToField = insertJniType("SetObjectField"),
        insertToStaticField = insertJniType("SetStaticObjectField"),
        transformToCppLong = jStringToCppStringTransform(ptr = true)
    )


fun extractJniString(method: String) = ExtractJniType { variable, jvmObj, fieldOrMethodId ->
    " ( jstring )  env->${method}($jvmObj, $fieldOrMethodId)"
}


fun jStringToCppStringTransform(ptr: Boolean = false) = object : TransformJniTypeLong {
    override fun transform(jniVariable: String, cppVariable: String) = Poet().apply {
        val tempCppVariable = "tempStr${unicFieldIndex++}"
        metas[JStringTransformMeta::class.qualifiedName!!] = JStringTransformMeta(
            jniVariable = jniVariable,
            tempCppVariable = tempCppVariable
        )

        statement("const char *$tempCppVariable = env->GetStringUTFChars(${jniVariable}, NULL)")
        if (ptr) {
            statement("$cppVariable = $tempCppVariable ? std::make_shared<std::string>( $tempCppVariable ) : std::shared_ptr<std::string>() ")
        } else {
            statement("$cppVariable = $tempCppVariable ?: \"\" ")
        }
    }

    override fun release(transform: Poet): Poet = Poet().apply {
        val metas = metas[JStringTransformMeta::class.qualifiedName!!] as JStringTransformMeta
        statement("if (${metas.tempCppVariable}) env->ReleaseStringUTFChars( ${metas.jniVariable}, ${metas.tempCppVariable})")
    }

}