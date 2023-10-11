#include <jni.h>
#include <string>
#include "brooklyn.h"

using namespace brooklyn::mapper;

extern "C" JNIEXPORT jint JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_initLib(
        JNIEnv *env,
        jclass /* class */
) {
    int initResult = brooklyn::mapper::init(env);
    return initResult;
}



extern "C" JNIEXPORT jint JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_deinitLib(
        JNIEnv *env,
        jclass /* class */
) {
    int deinitResult = brooklyn::mapper::deinit(env);
    return deinitResult;
}



extern "C" JNIEXPORT jobject JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_copySimple(
        JNIEnv *env,
        jclass /* class */,
        jobject jvmSimple
) {
    auto simple = ComKlee0kaiExampleModelSimple_mapping::mapFromJvm(env, jvmSimple);

    simple->age++;
    *simple->name = simple->name->append("from c++");
    simple->address = simple->address.append("from c++");

    auto jvmSimple2 = ComKlee0kaiExampleModelSimple_mapping::mapToJvm(env, simple);
    return jvmSimple2;
}



extern "C" JNIEXPORT jobject JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_copyNullableType(
        JNIEnv *env,
        jclass /* class */,
        jobject jvmNullableType
) {
    auto cppObject = ComKlee0kaiExampleModelNullableTypePojo_mapping::mapFromJvm(env, jvmNullableType);


    auto jvmSimple2 = ComKlee0kaiExampleModelNullableTypePojo_mapping::mapToJvm(env, cppObject);
    return jvmSimple2;
}
