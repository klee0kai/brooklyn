package com.github.klee0kai.brooklyn.cpp.mapper.std

import com.github.klee0kai.brooklyn.cpp.common.line
import com.github.klee0kai.brooklyn.cpp.common.statement
import com.github.klee0kai.brooklyn.poet.Poet

fun Poet.byteIndexInit() = apply {
    statement("if (byteIndex) return 0")
    statement("byteIndex = std::make_shared<SimpleIndexStructure>()")
    statement("byteIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Byte\") )")
    statement("byteIndex->toJvm = env->GetMethodID(byteIndex->cls, \"<init>\",\"(B)V\" ) ")
    statement("if (!byteIndex->toJvm) return -1")
    statement("byteIndex->fromJvm = env->GetMethodID(byteIndex->cls, \"byteValue\",\"()B\") ")
    statement("if (!byteIndex->fromJvm) return -1")
}


fun Poet.mapByteFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJByteBoxed(JNIEnv *env, jobject jValue) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jValue ? std::make_shared<int>( int(env->CallByteMethod(jValue, byteIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapByteToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJByteBoxed(JNIEnv *env, const std::shared_ptr<int>& valuePtr) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return valuePtr ? env->NewObject(byteIndex->cls, byteIndex->toJvm, jbyte( *valuePtr ) ) : NULL")
    line("}")
}


fun Poet.mapByteArrayFromJava(isImpl: Boolean = false) =apply {
    mapPrimitiveArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJByteArray",
        cppType = "int",
        jType = "jbyte",
        jArrayType = "jbyteArray",
        jGetElementsMethod = "GetByteArrayElements",
        jReleaseArrayMethod = "ReleaseByteArrayElements"
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBoxedByteArray",
        cppType = "int",
        mappingMethod = { variable -> "*mapFromJByteBoxed(env, $variable )" }
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJByteNullableArray",
        cppType = "std::shared_ptr<int>",
        mappingMethod = { variable -> "mapFromJByteBoxed(env, $variable )" }
    )
}


fun Poet.mapByteArrayToJava(isImpl: Boolean = false) = apply {
    mapPrimitiveArrayToJvm(
        isImpl = isImpl,
        name = "mapToJByteArray",
        cppType = "int",
        jType = "jbyte",
        jArrayType = "jbyteArray",
        jCreateArrayMethod = "NewByteArray",
        jSetArrayMethod = "SetByteArrayRegion",
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBoxedByteArray",
        cppType = "int",
        indexVariable = "byteIndex",
        mappingMethod = { variable -> "mapToJByteBoxed(env, std::make_shared<int>( $variable ) )" }
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJByteNullableArray",
        cppType = "std::shared_ptr<int>",
        indexVariable = "byteIndex",
        mappingMethod = { variable -> "mapToJByteBoxed(env, $variable )" }
    )
}


