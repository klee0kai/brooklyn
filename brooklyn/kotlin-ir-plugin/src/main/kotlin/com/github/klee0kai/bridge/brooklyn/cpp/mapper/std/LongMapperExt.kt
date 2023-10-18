package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.longIndexInit() = apply {
    statement("if (longIndex) return 0")
    statement("longIndex = std::make_shared<SimpleIndexStructure>()")
    statement("longIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Long\") )")
    statement("longIndex->toJvm = env->GetMethodID(longIndex->cls, \"<init>\",\"(J)V\" ) ")
    statement("if (!longIndex->toJvm) return -1")
    statement("longIndex->fromJvm = env->GetMethodID(longIndex->cls, \"longValue\",\"()J\") ")
    statement("if (!longIndex->fromJvm) return -1")
}


fun Poet.mapLongFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int64_t> mapFromJLongBoxed(JNIEnv *env, jobject jLong) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jLong ? std::make_shared<int64_t>( int64_t(env->CallLongMethod(jLong, longIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int64_t>()")
    line("}")
}


fun Poet.mapLongToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJLongBoxed(JNIEnv *env, const std::shared_ptr<int64_t>& cppLong) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return cppLong ? env->NewObject(longIndex->cls, longIndex->toJvm, jlong( *cppLong ) ) : NULL")
    line("}")
}


fun Poet.mapLongArrayFromJava(isImpl: Boolean = false) =
    mapPrimitiveTypeToJvm(
        isImpl = isImpl,
        name = "mapFromJLongArray",
        cppType = "int64_t",
        jType = "jlong",
        jArrayType = "jlongArray",
        jGetElementsMethod = "GetLongArrayElements",
        jReleaseArrayMethod = "ReleaseLongArrayElements"
    )


fun Poet.mapLongArrayToJava(isImpl: Boolean = false) =
    mapPrimitiveTypeFromJvm(
        isImpl = isImpl,
        name = "mapToJLongArray",
        cppType = "int64_t",
        jType = "jlong",
        jArrayType = "jlongArray",
        jCreateArrayMethod = "NewLongArray",
        jSetArrayMethod = "SetLongArrayRegion",
    )
