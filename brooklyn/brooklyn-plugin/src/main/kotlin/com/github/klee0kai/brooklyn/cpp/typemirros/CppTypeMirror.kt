package com.github.klee0kai.brooklyn.cpp.typemirros

import com.github.klee0kai.brooklyn.cpp.common.CommonNaming.BROOKLYN
import com.github.klee0kai.brooklyn.cpp.common.CommonNaming.MAPPER
import com.github.klee0kai.brooklyn.cpp.common.camelCase
import com.github.klee0kai.brooklyn.cpp.common.croppedPackageName
import com.github.klee0kai.brooklyn.cpp.common.firstUppercase
import com.github.klee0kai.brooklyn.cpp.common.snakeCase
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.ClassId

fun interface ExtractJniType {
    /**
     *  extract jstring from field or method
     */
    fun invoke(
        type: CppTypeMirror,
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
    val cppSimpleTypeMirrorStr: String,
    cppFullTypeMirror: String? = null,
    val jniTypeStr: String = "jobject",

    val classId: ClassId? = null,

    val checkIrType: (IrType) -> Boolean = { false },
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
    val cppFullTypeMirror = cppFullTypeMirror ?: cppSimpleTypeMirrorStr
}

fun IrType.jniType(): CppTypeMirror? {
    val typeCl = getClass()
    return allCppTypeMirrors.firstOrNull { typeMirror ->
        typeMirror.checkIrType(this)
                || typeCl?.let { typeMirror.checkIrClass(typeCl, isNullable()) } == true
    }
}

fun IrProperty.jniType() = getter?.returnType?.jniType()

fun IrField.jniType() = type.jniType()

fun IrClass.jniType(nullable: Boolean = true): CppTypeMirror? =
    allCppTypeMirrors.firstOrNull { it.checkIrClass(this, nullable) }


fun IrClass.cppModelMirror() = classId?.let { classId ->
    "${classId.croppedPackageName}${classId.shortClassName}".camelCase().firstUppercase()
}

fun IrClass.cppMappingNameSpace() = cppModelMirror()?.let { "${it}_mapping" } ?: "mapping"


// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html
val allCppTypeMirrors: MutableList<CppTypeMirror> = mutableListOf(
    *primitiveTypeMirrors(),
    *primitiveArraysTypeMirrors(),
    *boxedTypeMirrors(),
    *stringsTypeMirror(),
    *boxedArraysTypeMirrors(),
)


fun extractJniType(method: String) = ExtractJniType { type, jvmObj, fieldOrMethodId, args ->
    val castType = if (type.jniTypeStr != "jobject") "( ${type.jniTypeStr}  )" else "";
    " $castType env->${method}( ${listOf(jvmObj, fieldOrMethodId, args).joinArgs()} )"
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
            cppSimpleTypeMirrorStr = cppModelMirror,
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
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::shared_ptr<${cppModelMirror}>",
            jniTypeStr = "jobject",
            classId = classId,
            checkIrClass = { cl, _ -> cl.classId == clazz.classId },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapToJvm(env,  $variable )" },
            transformToCpp = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::vector<${cppModelMirror}>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    !argType.isNullable() && argClass?.classId == clazz.classId
                } else false

            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayToJvm(env, std::make_shared<std::vector<${cppModelMirror}>>(  $variable ) )" },
            transformToCpp = { variable -> "*${BROOKLYN}::${MAPPER}::${namespace}::mapArrayFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::shared_ptr<std::vector<${cppModelMirror}>>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isNullableArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    !argType.isNullable() && argClass?.classId == clazz.classId
                } else false

            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayToJvm(env,  $variable )" },
            transformToCpp = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::vector<std::shared_ptr<${cppModelMirror}>>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    argClass?.classId == clazz.classId
                } else false
            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableToJvm(env, std::make_shared<std::vector<std::shared_ptr<${cppModelMirror}>>>(  $variable ) )" },
            transformToCpp = { variable -> "*${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<${cppModelMirror}>>>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isNullableArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    argClass?.classId == clazz.classId
                } else false

            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableToJvm(env, $variable )" },
            transformToCpp = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableFromJvm(env, $variable ) " },
        )
    )
}

fun MutableList<CppTypeMirror>.addSupportedMirrorClass(clazz: IrClass) {
    val classId = clazz.classId!!
    val cppModelMirror = clazz.cppModelMirror() ?: return
    val namespace = clazz.cppMappingNameSpace()
    add(
        CppTypeMirror(
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
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
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::shared_ptr<${cppModelMirror}>",
            jniTypeStr = "jobject",
            classId = classId,
            checkIrClass = { cl, _ -> cl.classId == clazz.classId },
            transformToJni = { variable -> "${variable}->jvmObject()" },
            transformToCpp = { variable -> "std::make_shared<${BROOKLYN}::${cppModelMirror}>( $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::vector<${cppModelMirror}>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    !argType.isNullable() && argClass?.classId == clazz.classId
                } else false

            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayToJvm(env, std::make_shared<std::vector<${cppModelMirror}>>(  $variable ) )" },
            transformToCpp = { variable -> "*${BROOKLYN}::${MAPPER}::${namespace}::mapArrayFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::shared_ptr<std::vector<${cppModelMirror}>>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isNullableArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    !argType.isNullable() && argClass?.classId == clazz.classId
                } else false

            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayToJvm(env,  $variable )" },
            transformToCpp = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::vector<std::shared_ptr<${cppModelMirror}>>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    argClass?.classId == clazz.classId
                } else false
            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableToJvm(env, std::make_shared<std::vector<std::shared_ptr<${cppModelMirror}>>>(  $variable ) )" },
            transformToCpp = { variable -> "*${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableFromJvm(env, $variable ) " },
        )
    )
    add(
        CppTypeMirror(
            jniTypeCode = "[L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppSimpleTypeMirrorStr = cppModelMirror,
            cppFullTypeMirror = "std::shared_ptr<std::vector<std::shared_ptr<${cppModelMirror}>>>",
            jniTypeStr = "jobjectArray",
            classId = classId,
            checkIrType = { type ->
                if (type.isNullableArray()) {
                    val argType = (type as? IrSimpleType)
                        ?.arguments
                        ?.getOrNull(0)
                        ?.typeOrNull
                        ?: return@CppTypeMirror false
                    val argClass = argType.getClass()
                    argClass?.classId == clazz.classId
                } else false

            },
            transformToJni = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableToJvm(env, $variable )" },
            transformToCpp = { variable -> "${BROOKLYN}::${MAPPER}::${namespace}::mapArrayNullableFromJvm(env, $variable ) " },
        )
    )
}




