package com.github.klee0kai.bridge.brooklyn.cpp.mapper.std

import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement
import com.github.klee0kai.bridge.brooklyn.poet.Poet

fun Poet.booleanIndexInit() = apply {
    statement("if (booleanIndex) return 0")
    statement("booleanIndex = std::make_shared<SimpleIndexStructure>()")
    statement("booleanIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/Boolean\") )")
    statement("booleanIndex->toJvm = env->GetMethodID(booleanIndex->cls, \"<init>\",\"(Z)V\" ) ")
    statement("if (!booleanIndex->toJvm) return -1")
    statement("booleanIndex->fromJvm = env->GetMethodID(booleanIndex->cls, \"booleanValue\",\"()Z\") ")
    statement("if (!booleanIndex->fromJvm) return -1")
}


fun Poet.mapBooleanFromJava(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<int> mapFromJBoolean(JNIEnv *env, jobject jBoolean) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    line("return jBoolean ? std::make_shared<int>( int(env->CallBooleanMethod(jBoolean, booleanIndex->fromJvm ))) ")
    statement(": std::shared_ptr<int>()")
    line("}")
}


fun Poet.mapBooleanToJava(isImpl: Boolean = false) = apply {
    val declare = "jobject mapToJBoolean(JNIEnv *env, const std::shared_ptr<int>& cppBoolean) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return cppBoolean ? env->NewObject(booleanIndex->cls,booleanIndex->toJvm, jboolean( *cppBoolean ) ) : NULL")

    line("}")
}


fun Poet.mapBooleanArrayFromJava(isImpl: Boolean = false) =
    mapPrimitiveTypeToJvm(
        isImpl = isImpl,
        name = "mapFromJBooleanArray",
        cppType = "int",
        jType = "jboolean",
        jArrayType = "jbooleanArray",
        jGetElementsMethod = "GetBooleanArrayElements",
        jReleaseArrayMethod = "ReleaseBooleanArrayElements"
    )


fun Poet.mapBooleanArrayToJava(isImpl: Boolean = false) =
    mapPrimitiveTypeFromJvm(
        isImpl = isImpl,
        name = "mapToJBooleanArray",
        cppType = "int",
        jType = "jboolean",
        jArrayType = "jbooleanArray",
        jCreateArrayMethod = "NewBooleanArray",
        jSetArrayMethod = "SetBooleanArrayRegion",
    )


fun Poet.mapPrimitiveTypeToJvm(
    isImpl: Boolean,
    name: String,
    cppType: String,
    jType: String,
    jArrayType: String,
    jGetElementsMethod: String,
    jReleaseArrayMethod: String,
) = apply {
    val declare =
        "std::shared_ptr<std::vector<${cppType}>> ${name}(JNIEnv *env, const ${jArrayType}& jarray) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!jarray)return {}")
    statement("int len = env->GetArrayLength(jarray)")
    statement("$jType *tempArray = env->${jGetElementsMethod}(jarray, NULL)")
    statement("auto array = std::vector<${cppType}>(len)")
    line("for (int i = 0; i < len; i++) {")
    statement("array[i] = ${cppType}(tempArray[i]);")
    line("}")
    statement("env->${jReleaseArrayMethod}(jarray, tempArray, 0)")
    statement("return std::make_shared<std::vector<${cppType}>>(array)")
    line("}")
}


fun Poet.mapPrimitiveTypeFromJvm(
    isImpl: Boolean,
    name: String,
    cppType: String,
    jType: String,
    jArrayType: String,
    jCreateArrayMethod: String,
    jSetArrayMethod: String,
) = apply {
    val declare = "$jArrayType ${name}(JNIEnv *env, const std::shared_ptr<std::vector<${cppType}>> &array) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!array)return NULL")
    statement("int len = array->size()")
    statement("$jType *tempArray = new $jType[len]")
    line("for (int i = 0; i < len; i++) {")
    statement("tempArray[i] = $jType((*array)[i])")
    line("}")
    statement("$jArrayType jarray = env->$jCreateArrayMethod(len)")
    statement("env->$jSetArrayMethod(jarray, 0, len, tempArray)")
    statement("delete[] tempArray")
    statement("return jarray")
    line("}")
}