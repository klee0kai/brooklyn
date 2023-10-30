package com.github.klee0kai.bridge.brooklyn.cpp.env

import com.github.klee0kai.bridge.brooklyn.cpp.common.CodeBuilder
import com.github.klee0kai.bridge.brooklyn.cpp.common.line
import com.github.klee0kai.bridge.brooklyn.cpp.common.statement

fun CodeBuilder.envCppVariables() = apply {
    header {
        statement("using namespace std")
    }
    variables {
        statement(
            "struct EnvBind {\n" +
                    " int attached = 0;\n" +
                    " JNIEnv *env = NULL;\n" +
                    "}"
        )
        statement("static map <thread::id, EnvBind> envs = {}")
        statement("static JavaVM *g_vm")
    }
}

fun CodeBuilder.initEnvJvm(isImpl: Boolean = false) = apply {
    val declare = "int initJvm(JavaVM *pVM)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("JNIEnv *env = NULL")
    statement("pVM->GetEnv((void **) &env, JNI_VERSION_1_6)")
    statement("return init(env)")
    line("}")
}

fun CodeBuilder.initEnv(isImpl: Boolean = false) = apply {
    val declare = "int init(JNIEnv *env)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("return mapper::init(env) || env->GetJavaVM(&g_vm)")
    line("}")
}

fun CodeBuilder.deInitEnv(isImpl: Boolean = false) = apply {
    val declare = "int deinit(JNIEnv *env)"
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("mapper::deinit(env)")
    statement("envs.clear()")
    statement("g_vm = NULL;")
    line("}")
}


fun CodeBuilder.getEnv(isImpl: Boolean = false) = apply {
    val declare = "JNIEnv* env() "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("thread::id id = this_thread::get_id()")
    statement("return envs[id].env")
    line("}")
}


fun CodeBuilder.attachEnv(isImpl: Boolean = false) = apply {
    val declare = "int attachThread() "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("thread::id id = this_thread::get_id()")
    statement("if (envs.find(id) != envs.end()) return 0")
    statement(" JNIEnv *g_env = NULL")

    statement("int getEnvStat = g_vm->GetEnv((void **) &g_env, JNI_VERSION_1_6)")
    line("if (getEnvStat == JNI_EDETACHED) {")
    statement("int attachResult = g_vm->AttachCurrentThread((JNIEnv **) &g_env, NULL) != 0")
    statement("if (attachResult != 0)return attachResult")
    statement("envs[id] = EnvBind{ .attached = 1, .env =g_env } ")
    line("} else if (getEnvStat == JNI_OK) { ")
    statement("envs[id] = EnvBind{ .attached = 0, .env =g_env } ")
    line(" } else if (getEnvStat == JNI_EVERSION) {")
    statement("return getEnvStat")
    line("}")
    line("}")
}


fun CodeBuilder.detactEnv(isImpl: Boolean = false) = apply {
    val declare = "void detachThread() "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("thread::id id = this_thread::get_id()")
    statement("auto it = envs.find(id)")
    statement("if (it != envs.end()) return")
    line("if (it->second.attached) {")
    statement("g_vm->DetachCurrentThread()")
    line("}")
    statement("envs.erase(it)")
    line("}")
}

fun CodeBuilder.bindEnv(isImpl: Boolean = false) = apply {
    val declare = "void bindEnv(JNIEnv *env) "
    if (!isImpl) {
        statement(declare)
        return@apply
    }
    line("$declare {")
    statement("thread::id id = this_thread::get_id()")
    statement("if (envs.find(id) != envs.end()) return")
    statement("envs[id] = EnvBind{.attached = 0, .env =env}")
    line("}")
}



