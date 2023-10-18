package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.floatIndexInit() = apply {
    statement("if (floatIndex) return 0")
    statement("floatIndex = std::make_shared<SimpleIndexStructure>()")
    statement("floatIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Float\") )")
    statement("floatIndex->toJvm = env->GetMethodID(floatIndex->cls, \"<init>\",\"(F)V\" ) ")
    statement("if (!floatIndex->toJvm) return -1")
    statement("floatIndex->fromJvm = env->GetMethodID(floatIndex->cls, \"floatValue\",\"()F\") ")
    statement("if (!floatIndex->fromJvm) return -1")
}


fun Poet.mapFloatFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<float> mapFromJFloatBoxed(JNIEnv *env, jobject jFloat) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jFloat ? std::make_shared<float>( float(env->CallFloatMethod(jFloat, floatIndex->fromJvm ))) ")
    statement(": std::shared_ptr<float>()")
    line("}")
}


fun Poet.mapFloatToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJFloatBoxed(JNIEnv *env, const std::shared_ptr<float>& valuePtr) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return valuePtr ? env->NewObject(floatIndex->cls, floatIndex->toJvm, jfloat( *valuePtr ) ) : NULL")
    line("}")
}

fun Poet.mapFloatArrayFromJava(isImpl: Boolean = false) = apply {
    mapPrimitiveArrayFromJvm(
        isImpl = isImpl,
        name = "mapToJFloatArray",
        cppType = "float",
        jType = "jfloat",
        jArrayType = "jfloatArray",
        jCreateArrayMethod = "NewFloatArray",
        jSetArrayMethod = "SetFloatArrayRegion",
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBoxedFloatArray",
        cppType = "float",
        mappingMethod = { variable -> "*mapFromJFloatBoxed(env, $variable )" }
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJFloatNullableArray",
        cppType = "std::shared_ptr<float>",
        mappingMethod = { variable -> "mapFromJFloatBoxed(env, $variable )" }
    )
}


fun Poet.mapFloatArrayToJava(isImpl: Boolean = false) = apply {
    mapPrimitiveArrayToJvm(
        isImpl = isImpl,
        name = "mapFromJFloatArray",
        cppType = "float",
        jType = "jfloat",
        jArrayType = "jfloatArray",
        jGetElementsMethod = "GetFloatArrayElements",
        jReleaseArrayMethod = "ReleaseFloatArrayElements"
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBoxedFloatArray",
        cppType = "float",
        indexVariable = "floatIndex",
        mappingMethod = { variable -> "mapToJFloatBoxed(env, std::make_shared<float>( $variable ) )" }
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJFloatNullableArray",
        cppType = "std::shared_ptr<float>",
        indexVariable = "floatIndex",
        mappingMethod = { variable -> "mapToJFloatBoxed(env, $variable )" }
    )
}
