package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import com.github.klee0kai.bridge.brooklyn.cpp.common.camelCase
import com.github.klee0kai.bridge.brooklyn.cpp.common.firstCamelCase
import com.github.klee0kai.bridge.brooklyn.cpp.common.snakeCase
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.kotlinFqName

fun interface ExtractJniType {
    /**
     *  extract jstring from field or method
     */
    fun invoke(jvmObj: String, fieldOrMethodId: String): String
}

fun interface InsertJniType {
    /**
     * jstring to field
     */
    fun invoke(variable: String, jvmObj: String, fieldOrMethodId: String): String
}

fun interface TransformJniType {
    /**
     * str::string -> jstring
     * or
     * jstring -> str::string
     */
    fun invoke(variable: String): String
}

class CppTypeMirror(
    val jniTypeStr: String="jobject",
    val jniTypeCode: String,
    val cppTypeMirrorStr: String,

    val checkIrType: (IrType) -> Boolean = { false },
    val checkIrClass: (IrClass, nullable: Boolean) -> Boolean = { _, _ -> false },

    val transformToJni: TransformJniType = todoCreate,
    val transformToCpp: TransformJniType = todoCreate,

    val extractFromField: ExtractJniType = extractJniType("GetObjectField"),
    val extractFromStaticField: ExtractJniType = extractJniType("GetStaticObjectField"),
    val extractFromMethod: ExtractJniType = extractJniType("CallObjectMethod"),
    val extractFromStaticMethod: ExtractJniType = extractJniType("CallStaticObjectMethod"),

    val insertToField: InsertJniType = insertJniType("SetObjectField"),
    val insertToStaticField: InsertJniType = insertJniType("SetStaticObjectField"),

    )

fun IrType.jniType(): CppTypeMirror? {
    val typeCl = getClass()
    return allCppTypeMirrors.firstOrNull { typeMirror ->
        typeMirror.checkIrType(this)
                || typeCl?.let { typeMirror.checkIrClass(typeCl, isNullable()) } == true
    }
}

fun IrClass.jniType(nullable: Boolean = true): CppTypeMirror? =
    allCppTypeMirrors.firstOrNull { it.checkIrClass(this, nullable) }


// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html
val allCppTypeMirrors: MutableList<CppTypeMirror> = mutableListOf(
    *primitiveTypeMirrors(),
    *boxedTypeMirrors(),
    stringTypeMirror(),
    stringNullableTypeMirror(),
)

@Deprecated("todo")
private val todoCreate get() = TransformJniType { TODO() }


fun extractJniType(method: String) = ExtractJniType { jvmObj, fieldOrMethodId ->
    "env->${method}($jvmObj, $fieldOrMethodId)"
}

fun insertJniType(method: String) = InsertJniType { variable, jvmObj, fieldOrMethodId ->
    "env->${method}($jvmObj, $fieldOrMethodId, $variable )"
}


fun MutableList<CppTypeMirror>.addSupportedPojoClass(clazz: IrClass) {
    val classId = clazz.classId!!
    val cppModelMirror = "${classId.packageFqName}${classId.shortClassName}".camelCase().firstCamelCase()
    add(
        CppTypeMirror(
            jniTypeStr = "jobject",
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppTypeMirrorStr = cppModelMirror,
            checkIrClass = { cl, _ -> cl.classId == clazz.classId },

            ),
    )
}



