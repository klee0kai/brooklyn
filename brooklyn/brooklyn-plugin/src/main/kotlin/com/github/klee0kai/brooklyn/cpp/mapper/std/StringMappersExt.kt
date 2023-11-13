package com.github.klee0kai.brooklyn.cpp.mapper.std

import com.github.klee0kai.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.brooklyn.cpp.common.line
import com.github.klee0kai.brooklyn.cpp.common.statement
import com.github.klee0kai.brooklyn.poet.Poet


fun Poet.stringIndexInit() = apply {
    statement("if (stringIndex) return 0")
    statement("stringIndex = std::make_shared<SimpleIndexStructure>()")
    statement("stringIndex->cls = (jclass) env->NewGlobalRef( env->FindClass(\"java/lang/String\") )")
    statement("if (!stringIndex->cls) return -1")
}


fun CodeBuilder.mapFromJString(isImpl: Boolean = false) = apply {
    val declare = "std::shared_ptr<std::string> mapJString(JNIEnv *env, jstring jString) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare {")
        statement("const char * tempChars = jString!= NULL ? env->GetStringUTFChars(jString, NULL) : NULL ")
        statement("std::shared_ptr<std::string> cStr = tempChars ? std::make_shared<std::string>( tempChars ) : std::shared_ptr<std::string>() ")
        statement("if ( tempChars ) env->ReleaseStringUTFChars(jString, tempChars)")
        statement("return cStr")
        line("}")
    }
}


fun CodeBuilder.mapToJString(isImpl: Boolean = false) = apply {
    val declare = "jstring mapToJString(JNIEnv *env, const std::shared_ptr<std::string>& str) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare { ")
        statement("return str ? env->NewStringUTF( str->c_str() ) : NULL ")
        line("}")
    }
}

fun CodeBuilder.mapFromJStringArray(isImpl: Boolean = false) = apply {
    val declare =
        "std::shared_ptr<std::vector<std::string>> mapFromJStringArray(JNIEnv *env, const jobjectArray &jarray) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare { ")
        statement("if (!jarray)return {}")
        statement("int len = env->GetArrayLength(jarray)")
        statement("auto array = std::vector<std::string>( len )")
        line("for (int i = 0; i < len; i++) {")
        statement("array[i] = *mapJString(env, (jstring) env->GetObjectArrayElement(jarray, i))")
        line("}")
        statement("return std::make_shared<std::vector<std::string>>(array)")
        line("}")
    }
}


fun CodeBuilder.mapFromJStringArrayNullable(isImpl: Boolean = false) = apply {
    val declare =
        "std::shared_ptr<std::vector<std::shared_ptr<std::string>>> mapFromJStringArrayNullable(JNIEnv *env, const jobjectArray &jarray) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare { ")
        statement("if (!jarray)return {}")
        statement("int len = env->GetArrayLength(jarray)")
        statement("auto array = vector<shared_ptr<string>>( len )")
        line("for (int i = 0; i < len; i++) {")
        statement("array[i] = mapJString(env, (jstring) env->GetObjectArrayElement(jarray, i))")
        line("}")
        statement("return make_shared<vector<shared_ptr<string>>>(array)")
        line("}")
    }
}


fun CodeBuilder.mapToJStringArray(isImpl: Boolean = false) = apply {
    val declare =
        "jobjectArray mapToJStringArray(JNIEnv *env, const std::shared_ptr<std::vector<std::string>> &array)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare { ")
        statement("if (!array)return NULL")
        statement("int len = array->size()")
        statement("auto jarray = env->NewObjectArray(len, stringIndex->cls, NULL)")
        line("for (int i = 0; i < len; i++) {")
        statement("env->SetObjectArrayElement(jarray, i, mapToJString(env, make_shared<string>((*array)[i])))")
        line("}")
        statement("return jarray")
        line("}")
    }
}


fun CodeBuilder.mapToJStringArrayNullable(isImpl: Boolean = false) = apply {
    val declare =
        "jobjectArray mapToJStringArrayNullable(JNIEnv *env, const std::shared_ptr<std::vector<std::shared_ptr<std::string>>> &array)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    body {
        line("$declare { ")
        statement("if (!array)return NULL")
        statement("int len = array->size()")
        statement("auto jarray = env->NewObjectArray(len, stringIndex->cls, NULL)")
        line("for (int i = 0; i < len; i++) {")
        statement("env->SetObjectArrayElement(jarray, i, array ? mapToJString(env, (*array)[i]) : NULL )")
        line("}")
        statement("return jarray")
        line("}")
    }
}