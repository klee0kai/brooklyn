package com.github.klee0kai.bridge.brooklyn.cpp.common

import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.kotlinFqName

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
    CppTypeMirror(
        jniTypeStr = "jboolean",
        jniTypeCode = "Z",
        cppTypeMirrorStr = "int",
        checkIrType = { it.isBoolean() },
        mapFromJvmField = simpleGetMethodCall("GetBooleanField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticBooleanField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallBooleanMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticBooleanMethod"),
        mapToJvmField = simpleSetMethodCall("SetBooleanField", variableCast = "jboolean"),
        mapToJvmSetMethod = simpleSetMethodCall("CallBooleanMethod", variableCast = "jboolean"),
    ),
    CppTypeMirror(
        jniTypeStr = "jbyte",
        jniTypeCode = "B",
        cppTypeMirrorStr = "int",
        checkIrType = { it.isByte() },
        mapFromJvmField = simpleGetMethodCall("GetByteField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticByteField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallByteMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticByteMethod"),
        mapToJvmField = simpleSetMethodCall("SetByteField", variableCast = "jbyte"),
        mapToJvmSetMethod = simpleSetMethodCall("CallByteMethod", variableCast = "jbyte"),
    ),
    CppTypeMirror(
        jniTypeStr = "jchar",
        jniTypeCode = "C",
        cppTypeMirrorStr = "char",
        checkIrType = { it.isChar() },
        mapFromJvmField = simpleGetMethodCall("GetCharField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticCharField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallCharMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticCharMethod"),
        mapToJvmField = simpleSetMethodCall("SetCharField", variableCast = "jchar"),
        mapToJvmSetMethod = simpleSetMethodCall("CallCharMethod", variableCast = "jchar"),
    ),
    CppTypeMirror(
        jniTypeStr = "jshort",
        jniTypeCode = "S",
        cppTypeMirrorStr = "int",
        checkIrType = { it.isShort() },
        mapFromJvmField = simpleGetMethodCall("GetShortField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticShortField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallShortMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticShortMethod"),
        mapToJvmField = simpleSetMethodCall("SetShortField", variableCast = "jshort"),
        mapToJvmSetMethod = simpleSetMethodCall("CallShortMethod", variableCast = "jshort"),
    ),
    CppTypeMirror(
        jniTypeStr = "jint",
        jniTypeCode = "I",
        cppTypeMirrorStr = "int",
        checkIrType = { it.isInt() },
        mapFromJvmField = simpleGetMethodCall("GetIntField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticIntField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallIntMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticIntMethod"),
        mapToJvmField = simpleSetMethodCall("SetIntField", variableCast = "jint"),
        mapToJvmSetMethod = simpleSetMethodCall("CallIntMethod", variableCast = "jint"),
    ),
    CppTypeMirror(
        jniTypeStr = "jlong",
        jniTypeCode = "J",
        cppTypeMirrorStr = "int64_t",
        checkIrType = { it.isLong() },
        mapFromJvmField = simpleGetMethodCall("GetLongField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticLongField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallLongMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticLongMethod"),
        mapToJvmField = simpleSetMethodCall("SetLongField", variableCast = "jlong"),
        mapToJvmSetMethod = simpleSetMethodCall("CallLongMethod", variableCast = "jlong"),
    ),
    CppTypeMirror(
        jniTypeStr = "jfloat",
        jniTypeCode = "F",
        cppTypeMirrorStr = "float",
        checkIrType = { it.isFloat() },
        mapFromJvmField = simpleGetMethodCall("GetFloatField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticFloatField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallFloatMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticFloatMethod"),
        mapToJvmField = simpleSetMethodCall("SetFloatField", variableCast = "jfloat"),
        mapToJvmSetMethod = simpleSetMethodCall("CallFloatMethod", variableCast = "jfloat"),
    ),
    CppTypeMirror(
        jniTypeStr = "jdouble",
        jniTypeCode = "D",
        cppTypeMirrorStr = "double",
        checkIrType = { it.isDouble() },
        mapFromJvmField = simpleGetMethodCall("GetDoubleField"),
        mapFromJvmStaticField = simpleGetMethodCall("GetStaticDoubleField"),
        mapFromJvmGetMethod = simpleGetMethodCall("CallDoubleMethod"),
        mapFromJvmGetStaticMethod = simpleGetMethodCall("CallStaticDoubleMethod"),
        mapToJvmField = simpleSetMethodCall("SetDoubleField", variableCast = "jdouble"),
        mapToJvmSetMethod = simpleSetMethodCall("CallDoubleMethod", variableCast = "jdouble"),
    ),

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


private fun simpleGetMethodCall(name: String): MapJvmVariable {
    return MapJvmVariable { variable: String, env: String, jvmObj: String, methodId: String ->
        Poet().apply {
            statement("$variable = ${env}->${name}($jvmObj, $methodId)")
        }
    }
}

private fun simpleSetMethodCall(
    name: String,
    variableCast: String
): MapJvmVariable {
    return MapJvmVariable { variable: String, env: String, jvmObj: String, methodId: String ->
        Poet().apply {
            statement("${env}->${name}($jvmObj, $methodId, ($variableCast) $variable)")
        }
    }
}


@Deprecated("TODO")
private val simpleTodo get() = MapJvmVariable { variable, env, jvmObj, fieldOrMethodId -> TODO() }

