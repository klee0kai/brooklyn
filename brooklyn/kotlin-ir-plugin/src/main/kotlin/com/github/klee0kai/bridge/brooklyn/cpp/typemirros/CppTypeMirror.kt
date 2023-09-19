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

fun interface MapJvmVariable {
    fun invoke(variable: String, env: String, jvmObj: String, fieldOrMethodId: String): Poet
}

class CppTypeMirror(
    val jniTypeStr: String,
    val jniTypeCode: String,
    val cppTypeMirrorStr: String,

    val checkIrType: (IrType) -> Boolean = { false },
    val checkIrClass: (IrClass, nullable: Boolean) -> Boolean = { _, _ -> false },
    val mapFromJvmField: MapJvmVariable,
    val mapFromJvmStaticField: MapJvmVariable,
    val mapFromJvmGetMethod: MapJvmVariable,
    val mapFromJvmGetStaticMethod: MapJvmVariable,
    val mapToJvmField: MapJvmVariable,
    val mapToJvmSetMethod: MapJvmVariable,
)

fun IrType.jniType(): CppTypeMirror? {
    val type = allCppTypeMirrors.firstOrNull { it.checkIrType(this) }
    if (type != null) return type
    val typeCl = getClass() ?: return null
    return allCppTypeMirrors.firstOrNull { it.checkIrClass(typeCl, isNullable()) }
}

fun IrClass.jniType(nullable: Boolean = true): CppTypeMirror? =
    allCppTypeMirrors.firstOrNull { it.checkIrClass(this, nullable) }


// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html
val allCppTypeMirrors: MutableList<CppTypeMirror> = mutableListOf(
    *primitiveTypeMirrors(),
    stringTypeMirror(),
    stringNullableTypeMirror(),

    )


fun MutableList<CppTypeMirror>.addSupportedPojoClass(clazz: IrClass) {
    val classId = clazz.classId!!
    val cppModelMirror = "${classId.packageFqName}${classId.shortClassName}".camelCase().firstCamelCase()
    add(
        CppTypeMirror(
            jniTypeStr = "jobject",
            jniTypeCode = "L${clazz.kotlinFqName.toString().snakeCase("/")};",
            cppTypeMirrorStr = cppModelMirror,
            checkIrClass = { cl, _ -> cl.classId == clazz.classId },
            mapFromJvmField = simpleTodo,
            mapFromJvmStaticField = simpleTodo,
            mapFromJvmGetMethod = simpleTodo,
            mapFromJvmGetStaticMethod = simpleTodo,
            mapToJvmField = simpleTodo,
            mapToJvmSetMethod = simpleTodo,
        ),
    )
}

@Deprecated("TODO")
internal val simpleTodo get() = MapJvmVariable { _, _, _, _ -> TODO() }

