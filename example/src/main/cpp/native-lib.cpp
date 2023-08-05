#include <jni.h>
#include <string>

extern "C" JNIEXPORT jint JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_initLib(
        JNIEnv* env,
        jclass /* class */
){
    return 1;
}



extern "C" JNIEXPORT jint JNICALL
Java_com_klee0kai_example_engine_SimpleJniEngine_deinitLib(
        JNIEnv* env,
        jclass /* class */
){
    return 2;
}

