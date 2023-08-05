#include <jni.h>
#include <string>
#include "mappers/com_klee0kai_example_model_Simple_mapper.h"

extern "C" JNIEXPORT jint JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_initLib(
        JNIEnv* env,
        jclass /* class */
){
brooklyn::mapper::initComKlee0kaiExampleModelSimple()
    return 1;
}



extern "C" JNIEXPORT jint JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_deinitLib(
        JNIEnv* env,
        jclass /* class */
){
    return 2;
}

