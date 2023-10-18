package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.shortIndexInit() = apply {
    statement("if (shortIndex) return 0")
    statement("shortIndex = std::make_shared<SimpleIndexStructure>()")
    statement("shortIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Short\") )")
    statement("shortIndex->toJvm = env->GetMethodID(shortIndex->cls, \"<init>\",\"(S)V\" ) ")
    statement("if (!shortIndex->toJvm) return -1")
    statement("shortIndex->fromJvm = env->GetMethodID(shortIndex->cls, \"shortValue\",\"()S\") ")
    statement("if (!shortIndex->fromJvm) return -1")
}


fun Poet.mapShortFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJShortBoxed(JNIEnv *env, jobject jValue) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jValue ? std::make_shared<int>( int(env->CallShortMethod(jValue, shortIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapShortToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJShortBoxed(JNIEnv *env, const std::shared_ptr<int>& valuePtr) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return valuePtr ? env->NewObject(shortIndex->cls, shortIndex->toJvm, jshort( *valuePtr ) ) : NULL")
    line("}")
}


fun Poet.mapShortArrayFromJava(isImpl: Boolean = false) =apply {
    mapPrimitiveArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJShortArray",
        cppType = "int",
        jType = "jshort",
        jArrayType = "jshortArray",
        jGetElementsMethod = "GetShortArrayElements",
        jReleaseArrayMethod = "ReleaseShortArrayElements"
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBoxedShortArray",
        cppType = "int",
        mappingMethod = { variable -> "*mapFromJShortBoxed(env, $variable )" }
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJShortNullableArray",
        cppType = "std::shared_ptr<int>",
        mappingMethod = { variable -> "mapFromJShortBoxed(env, $variable )" }
    )
}


fun Poet.mapShortArrayToJava(isImpl: Boolean = false) =apply {
    mapPrimitiveArrayToJvm(
        isImpl = isImpl,
        name = "mapToJShortArray",
        cppType = "int",
        jType = "jshort",
        jArrayType = "jshortArray",
        jCreateArrayMethod = "NewShortArray",
        jSetArrayMethod = "SetShortArrayRegion",
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBoxedShortArray",
        cppType = "int",
        indexVariable = "shortIndex",
        mappingMethod = { variable -> "mapToJShortBoxed(env, std::make_shared<int>( $variable ) )" }
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJShortNullableArray",
        cppType = "std::shared_ptr<int>",
        indexVariable = "shortIndex",
        mappingMethod = { variable -> "mapToJShortBoxed(env, $variable )" }
    )
}
