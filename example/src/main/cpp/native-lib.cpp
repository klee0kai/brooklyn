#include <jni.h>
#include <string>
#include "brooklyn.h"

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
    int deinitResult = brooklyn::mapper::deinit();
    return deinitResult;
}

