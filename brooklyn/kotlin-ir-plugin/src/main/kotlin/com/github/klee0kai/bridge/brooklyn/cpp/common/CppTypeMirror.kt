package com.github.klee0kai.bridge.brooklyn.cpp.common

import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.*


class CppTypeMirror(
    val jniTypeStr: String,
    val jniTypeCode: String,
    val cppTypeMirrorStr: String,

    val checkIrType: (IrType) -> Boolean = { false },
    val checkIrClass: (IrClass) -> Boolean = { false },
    val mapFromJvmField: Poet.(variable: String, env: String, jvmObj: String, fieldId: String) -> Unit,
    val mapFromJvmStaticField: Poet.(variable: String, env: String, jvmObj: String, fieldId: String) -> Unit,
    val mapFromJvmGetMethod: Poet.(variable: String, env: String, jvmObj: String, methodId: String) -> Unit,
    val mapFromJvmGetStaticMethod: Poet.(variable: String, env: String, jvmObj: String, methodId: String) -> Unit,
    val mapToJvmField: Poet.(variable: String, env: String, jvmObj: String, fieldId: String) -> Unit,
    val mapToJvmSetMethod: Poet.(variable: String, env: String, jvmObj: String, methodId: String) -> Unit,
)


val allCppTypeMirrors = listOf(
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


private fun simpleGetMethodCall(name: String): Poet.(variable: String, env: String, jvmObj: String, methodId: String) -> Unit {
    return { variable: String, env: String, jvmObj: String, methodId: String ->
        statement("$variable = ${env}->${name}($jvmObj, $methodId)")
    }
}

private fun simpleSetMethodCall(
    name: String,
    variableCast: String
): Poet.(variable: String, env: String, jvmObj: String, methodId: String) -> Unit {
    return { variable: String, env: String, jvmObj: String, methodId: String ->
        statement("${env}->${name}($jvmObj, $methodId, ($variableCast) $variable)")
    }
}