package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.doubleIndexInit() = apply {
    statement("if (doubleIndex) return 0")
    statement("doubleIndex = std::make_shared<SimpleIndexStructure>()")
    statement("doubleIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Double\") )")
    statement("doubleIndex->toJvm = env->GetMethodID(doubleIndex->cls, \"<init>\",\"(D)V\" ) ")
    statement("if (!doubleIndex->toJvm) return -1")
    statement("doubleIndex->fromJvm = env->GetMethodID(doubleIndex->cls, \"doubleValue\",\"()D\") ")
    statement("if (!doubleIndex->fromJvm) return -1")
}


fun Poet.mapDoubleFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<double> mapFromJDoubleBoxed(JNIEnv *env, jobject jValue) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jValue ? std::make_shared<double>( double(env->CallDoubleMethod(jValue, doubleIndex->fromJvm ))) ")
    statement(": std::shared_ptr<double>()")
    line("}")
}


fun Poet.mapDoubleToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJDoubleBoxed(JNIEnv *env, const std::shared_ptr<double>& valuePtr) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return valuePtr ? env->NewObject(doubleIndex->cls, doubleIndex->toJvm, jdouble( *valuePtr ) ) : NULL")
    line("}")
}



fun Poet.mapDoubleArrayFromJava(isImpl: Boolean = false) =
    mapPrimitiveTypeToJvm(
        isImpl = isImpl,
        name = "mapFromJDoubleArray",
        cppType = "double",
        jType = "jdouble",
        jArrayType = "jdoubleArray",
        jGetElementsMethod = "GetDoubleArrayElements",
        jReleaseArrayMethod = "ReleaseDoubleArrayElements"
    )


fun Poet.mapDoubleArrayToJava(isImpl: Boolean = false) =
    mapPrimitiveTypeFromJvm(
        isImpl = isImpl,
        name = "mapToJDoubleArray",
        cppType = "double",
        jType = "jdouble",
        jArrayType = "jdoubleArray",
        jCreateArrayMethod = "NewDoubleArray",
        jSetArrayMethod = "SetDoubleArrayRegion",
    )
