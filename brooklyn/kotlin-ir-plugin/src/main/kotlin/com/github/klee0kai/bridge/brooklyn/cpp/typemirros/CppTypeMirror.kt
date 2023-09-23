package com.github.klee0kai.bridge.brooklyn.cpp.typemirros

import com.github.klee0kai.bridge.brooklyn.cpp.common.camelCase
import com.github.klee0kai.bridge.brooklyn.cpp.common.firstCamelCase
import com.github.klee0kai.bridge.brooklyn.cpp.common.snakeCase
import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.kotlinFqName

internal var unicFieldIndex = 1
    get() = field++

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

interface TransformJniTypeLong {
    /**
     * jstring -> str::string
     */
    fun transform(jniVariable: String, cppVariable: String): Poet

    /**
     * release const chart * after transform jstring -> str::string
     */
    fun release(transform: Poet): Poet
}

class CppTypeMirror(
    val jniTypeStr: String,
    val jniTypeCode: String,
    val cppTypeMirrorStr: String,

    val checkIrType: (IrType) -> Boolean = { false },
    val checkIrClass: (IrClass, nullable: Boolean) -> Boolean = { _, _ -> false },
    val extractFromField: ExtractJniType = todoExtract,
    val extractFromStaticField: ExtractJniType = todoExtract,
    val extractFromMethod: ExtractJniType = todoExtract,
    val extractFromStaticMethod: ExtractJniType = todoExtract,

    val transformToJni: TransformJniType = todoCreate,

    val insertToField: InsertJniType = todoInsert,
    val insertToStaticField: InsertJniType = todoInsert,


    val transformToCppShort: TransformJniType? = null,
    val transformToCppLong: TransformJniTypeLong? = null,
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
    stringTypeMirror(),
    stringNullableTypeMirror(),
)

@Deprecated("todo")
private val todoExtract get() = ExtractJniType { _, _ -> TODO() }

@Deprecated("todo")
private val todoInsert get() = InsertJniType { _, _, _ -> TODO() }

@Deprecated("todo")
private val todoCreate get() = TransformJniType { TODO() }


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



