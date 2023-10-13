package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.BROOKLYN
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.MAPPER
import com.github.klee0kai.bridge.brooklyn.cpp.common.camelCase
import com.github.klee0kai.bridge.brooklyn.cpp.common.firstCamelCase
import com.github.klee0kai.bridge.brooklyn.cpp.common.snakeCase
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.ClassId

fun interface ExtractJniType {
    /**
     *  extract jstring from field or method
     */
    fun invoke(
        jvmObj: String,
        fieldOrMethodId: String,
        args: String,
    ): String
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
    val jniTypeCode: String,
    val cppTypeMirrorStr: String,
    val jniTypeStr: String = "jobject",

    val isPtr: Boolean = false,
    val irType: IrType? = null,
    val classId: ClassId? = null,

    @Deprecated("use isPtr/isType/classId")
    val checkIrType: (IrType) -> Boolean = { false },
    @Deprecated("use isPtr/isType/classId")
    val checkIrClass: (IrClass, nullable: Boolean) -> Boolean = { _, _ -> false },

    val transformToJni: TransformJniType,
    val transformToCpp: TransformJniType,

    val extractFromField: ExtractJniType = extractJniType("GetObjectField"),
    val extractFromStaticField: ExtractJniType = extractJniType("GetStaticObjectField"),
    val extractFromMethod: ExtractJniType = extractJniType("CallObjectMethod"),
    val extractFromStaticMethod: ExtractJniType = extractJniType("CallStaticObjectMethod"),

    val insertToField: InsertJniType = insertJniType("SetObjectField"),
    val insertToStaticField: InsertJniType = insertJniType("SetStaticObjectField"),
) {
    val cppPtrTypeMirror get() = if (isPtr) "std::shared_ptr<${cppTypeMirrorStr}>" else cppTypeMirrorStr
}

fun IrType.jniType(): CppTypeMirror? {
    val typeCl = getClass()
    return allCppTypeMirrors.firstOrNull { typeMirror ->
        typeMirror.checkIrType(this)
                || typeCl?.let { typeMirror.checkIrClass(typeCl, isNullable()) } == true
    }
}

fun IrClass.jniType(nullable: Boolean = true): CppTypeMirror? =
    allCppTypeMirrors.firstOrNull { it.checkIrClass(this, nullable) }


fun IrClass.cppModelMirror() = classId?.let { classId ->
    "${classId.packageFqName}${classId.shortClassName}".camelCase().firstCamelCase()
}

fun IrClass.cppMappingNameSpace() = cppModelMirror()?.let { "${it}_mapping" } ?: "mapping"


// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html
val allCppTypeMirrors: MutableList<CppTypeMirror> = mutableListOf(
    *primitiveTypeMirrors(),
    *boxedTypeMirrors(),
    stringTypeMirror(),
    stringNullableTypeMirror(),
)


fun extractJniType(method: String) = ExtractJniType { jvmObj, fieldOrMethodId, args ->
    "env->${method}( ${listOf(jvmObj, fieldOrMethodId, args).joinArgs()} )"
}

fun insertJniType(method: String) = InsertJniType { variable, jvmObj, fieldOrMethodId ->
    "env->${method}($jvmObj, $fieldOrMethodId, $variable )"
}

fun List<String>.joinArgs() = filter { it.isNotBlank() }.joinToString(", ")


fun MutableList<CppTypeMirror>.addSupportedPojoClass(clazz: IrClass) {
    val classId = clazz.classId!!
    val cppModelMirror = clazz.cppModelMirror() ?: return
    val namespace = clazz.cppMappingNameSpace()
    add(
        CppTypeMirror(
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppTypeMirrorStr = cppModelMirror,
            jniTypeStr = "jobject",
            classId = classId,
            checkIrClass = { cl, nullable -> !nullable && cl.classId == clazz.classId },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapToJvm(env, std::make_shared<${cppModelMirror}>( $variable ) )" },
            transformToCpp = { variable -> "*${BROOKLYN}::${MAPPER}::${namespace}::mapFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppTypeMirrorStr = cppModelMirror,
            jniTypeStr = "jobject",
            isPtr = true,
            classId = classId,
            checkIrClass = { cl, _ -> cl.classId == clazz.classId },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapToJvm(env,  $variable )" },
            transformToCpp = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapFromJvm(env, $variable ) " },
        )
    )
}

fun MutableList<CppTypeMirror>.addSupportedMirrorClass(clazz: IrClass) {
    val classId = clazz.classId!!
    val cppModelMirror = clazz.cppModelMirror() ?: return
    add(
        CppTypeMirror(
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppTypeMirrorStr = cppModelMirror,
            jniTypeStr = "jobject",
            classId = classId,
            checkIrClass = { cl, nullable -> !nullable && cl.classId == clazz.classId },
            transformToJni = { variable -> "${variable}.jvmObject()" },
            transformToCpp = { variable -> "${BROOKLYN}::${cppModelMirror}( $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppTypeMirrorStr = cppModelMirror,
            jniTypeStr = "jobject",
            isPtr = true,
            classId = classId,
            checkIrClass = { cl, _ -> cl.classId == clazz.classId },
            transformToJni = { variable -> "${variable}->jvmObject()" },
            transformToCpp = { variable -> "std::make_shared<${BROOKLYN}::${cppModelMirror}>( $variable ) " },
        )
    )
}




