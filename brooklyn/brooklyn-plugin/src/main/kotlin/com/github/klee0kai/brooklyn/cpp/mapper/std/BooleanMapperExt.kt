package com.github.klee0kai.brooklyn.cpp.mapper.std

import com.github.klee0kai.brooklyn.cpp.common.line
import com.github.klee0kai.brooklyn.cpp.common.statement
import com.github.klee0kai.brooklyn.poet.Poet

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
    val declare = "std::shared_ptr<int> mapFromJBooleanBoxed(JNIEnv *env, jobject jBoolean) "
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
    val declare = "jobject mapToJBooleanBoxed(JNIEnv *env, const std::shared_ptr<int>& cppBoolean) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return cppBoolean ? env->NewObject(booleanIndex->cls,booleanIndex->toJvm, jboolean( *cppBoolean ) ) : NULL")

    line("}")
}


fun Poet.mapBooleanArrayFromJava(isImpl: Boolean = false) = apply {
    mapPrimitiveArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBooleanArray",
        cppType = "int",
        jType = "jboolean",
        jArrayType = "jbooleanArray",
        jGetElementsMethod = "GetBooleanArrayElements",
        jReleaseArrayMethod = "ReleaseBooleanArrayElements"
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBoxedBooleanArray",
        cppType = "int",
        mappingMethod = { variable -> "*mapFromJBooleanBoxed(env, $variable )" }
    )
    mapBoxedArrayFromJvm(
        isImpl = isImpl,
        name = "mapFromJBooleanNullableArray",
        cppType = "std::shared_ptr<int>",
        mappingMethod = { variable -> "mapFromJBooleanBoxed(env, $variable )" }
    )
}


fun Poet.mapBooleanArrayToJava(isImpl: Boolean = false) = apply {
    mapPrimitiveArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBooleanArray",
        cppType = "int",
        jType = "jboolean",
        jArrayType = "jbooleanArray",
        jCreateArrayMethod = "NewBooleanArray",
        jSetArrayMethod = "SetBooleanArrayRegion",
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBoxedBooleanArray",
        cppType = "int",
        indexVariable = "booleanIndex",
        mappingMethod = { variable -> "mapToJBooleanBoxed(env, std::make_shared<int>( $variable ) )" }
    )
    mapBoxedArrayToJvm(
        isImpl = isImpl,
        name = "mapToJBooleanNullableArray",
        cppType = "std::shared_ptr<int>",
        indexVariable = "booleanIndex",
        mappingMethod = { variable -> "mapToJBooleanBoxed(env, $variable )" }
    )
}


fun Poet.mapPrimitiveArrayFromJvm(
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


fun Poet.mapPrimitiveArrayToJvm(
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


fun Poet.mapBoxedArrayFromJvm(
    isImpl: Boolean,
    name: String,
    cppType: String,
    mappingMethod: (variable: String) -> String,
) = apply {
    val declare =
        "std::shared_ptr<std::vector<${cppType}>> ${name}(JNIEnv *env, const jobjectArray &jarray)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!jarray) return {}")
    statement("int len = env->GetArrayLength(jarray)")
    statement("auto array = std::vector<${cppType}>(len)")
    line("for (int i = 0; i < len; i++) {")
    statement("array[i] = ${mappingMethod.invoke(" env->GetObjectArrayElement(jarray, i)")}")
    line("}")
    statement("return std::make_shared<std::vector<${cppType}>>(array)")
    line("}")
}


fun Poet.mapBoxedArrayToJvm(
    isImpl: Boolean,
    name: String,
    cppType: String,
    indexVariable: String,
    mappingMethod: (variable: String) -> String,
) = apply {
    val declare =
        "jobjectArray ${name}(JNIEnv *env, const std::shared_ptr<std::vector<${cppType}>> &array)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("if (!array)return NULL")
    statement("int len = array->size()")
    statement("jobjectArray jarray = env->NewObjectArray(len, ${indexVariable}->cls, NULL)")
    line("for (int i = 0; i < len; i++) {")
    statement("env->SetObjectArrayElement(jarray, i, ${mappingMethod.invoke("(*array)[i] ) ")}")
    line("}")
    statement("return jarray")
    line("}")
}