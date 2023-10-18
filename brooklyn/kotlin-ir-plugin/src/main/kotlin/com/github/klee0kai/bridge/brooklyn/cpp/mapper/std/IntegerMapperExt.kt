package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.integerIndexInit() = apply {
    statement("if (integerIndex) return 0")
    statement("integerIndex = std::make_shared<SimpleIndexStructure>()")
    statement("integerIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Integer\") )")
    statement("integerIndex->toJvm = env->GetMethodID(integerIndex->cls, \"<init>\",\"(I)V\" ) ")
    statement("if (!integerIndex->toJvm) return -1")
    statement("integerIndex->fromJvm = env->GetMethodID(integerIndex->cls, \"intValue\",\"()I\") ")
    statement("if (!integerIndex->fromJvm) return -1")
}


fun Poet.mapIntegerFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJIntegerBoxed(JNIEnv *env, jobject jInt) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jInt ? std::make_shared<int>( int(env->CallIntMethod(jInt, integerIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapIntegerToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJIntegerBoxed(JNIEnv *env, const std::shared_ptr<int>& cppInt) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return cppInt ? env->NewObject(integerIndex->cls,integerIndex->toJvm, jint( *cppInt ) ) : NULL")

    line("}")
}

fun Poet.mapIntegerArrayFromJava(isImpl: Boolean = false) =apply {
    mapPrimitiveArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJIntegerArray",
        cppType = "int",
        jType = "jint",
        jArrayType = "jintArray",
        jGetElementsMethod = "GetIntArrayElements",
        jReleaseArrayMethod = "ReleaseIntArrayElements"
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBoxedIntegerArray",
        cppType = "int",
        mappingMethod = { variable -> "*mapFromJIntegerBoxed(env, $variable )" }
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJIntegerNullableArray",
        cppType = "std::shared_ptr<int>",
        mappingMethod = { variable -> "mapFromJIntegerBoxed(env, $variable )" }
    )
}


fun Poet.mapIntegerArrayToJava(isImpl: Boolean = false) =apply {
    mapPrimitiveArrayToJvm(
        isImpl = isImpl,
        name = "mapToJIntegerArray",
        cppType = "int",
        jType = "jint",
        jArrayType = "jintArray",
        jCreateArrayMethod = "NewIntArray",
        jSetArrayMethod = "SetIntArrayRegion",
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBoxedIntegerArray",
        cppType = "int",
        indexVariable = "integerIndex",
        mappingMethod = { variable -> "mapToJIntegerBoxed(env, std::make_shared<int>( $variable ) )" }
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJIntegerNullableArray",
        cppType = "std::shared_ptr<int>",
        indexVariable = "integerIndex",
        mappingMethod = { variable -> "mapToJIntegerBoxed(env, $variable )" }
    )
}
