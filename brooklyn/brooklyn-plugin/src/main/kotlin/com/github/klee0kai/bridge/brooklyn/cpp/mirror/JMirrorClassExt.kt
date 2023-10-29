package com.github.klee0kai.bridge.brooklyn.cpp.mirror

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.BROOKLYN
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.MAPPER
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppMappingNameSpace
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppModelMirror
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.joinArgs
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.*

fun CodeBuilder.declareClassMirror(jClass: IrClass) = apply {
    val usedTypes = mutableSetOf<IrType>()

    body {
        val clMirror = jClass.cppModelMirror() ?: return@body
        lines(1)
        line("class $clMirror {")
        line("public: ")

        statement("${clMirror}(jobject jvmSelf)")
        statement("${clMirror}(const ${clMirror}& other)")

        if (!jClass.isObject) jClass.constructors.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            val args = func.mirrorFuncArgs(env = true)?.joinToString(", ") ?: return@forEach
            usedTypes.addAll(func.allUsedTypes())

            statement("${clMirror}($args)")
        }

        jClass.functions.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            val args = func.mirrorFuncArgs()?.joinToString(", ") ?: return@forEach
            val returnType = func.returnType.jniType()?.cppFullTypeMirror ?: "void"
            usedTypes.addAll(func.allUsedTypes())

            if (jClass.isObject) {
                post("static ")
            }
            statement("$returnType ${func.name}($args)")
        }

        jClass.properties.forEach { prop ->
            if (prop.isExternal || prop.isIgnoringJni) return@forEach
            val type = prop.jniType()
                ?.cppFullTypeMirror
                ?: return@forEach

            if (jClass.isObject) post("static ")
            statement("$type get${prop.nameUpperCase}()")


            if (!prop.isVar) return@forEach
            if (jClass.isObject) post("static ")
            statement("void set${prop.nameUpperCase}( const ${type}& value )")
        }

        statement("~${clMirror}()")
        line("jobject jvmObject() { return jvmSelf; }")
        line("private: ")
        statement("jobject jvmSelf")
        statement("jobject jvmSelfRef")
        statement("std::shared_ptr<int> owners")
        statement("}")
    }


    header {
        usedTypes.mapNotNull { type ->
            type.jniType()?.classId
        }.toSet().forEach { classId ->
            include(classId.modelHeaderFile.path)
        }
    }
}


fun CodeBuilder.implementClassMirror(jClass: IrClass) = apply {
    val usedTypes = mutableSetOf<IrType>()

    header {
        statement("using namespace ${BROOKLYN}::${MAPPER}")
    }
    body {
        val clMirror = jClass.cppModelMirror() ?: return@body
        val mappingNamespace = jClass.cppMappingNameSpace()
        val indexField = jClass.classId!!.indexVariableName
        lines(1)

        line("${clMirror}::${clMirror}(jobject jvmSelf) {")
        statement("owners = std::make_shared<int>(1)")
        statement("JNIEnv* env = ${BROOKLYN}::env()")
        statement("${clMirror}::jvmSelf = jvmSelf")
        statement("${clMirror}::jvmSelfRef = env->NewGlobalRef(jvmSelf)")
        line("}")

        line("${clMirror}::${clMirror}(const ${clMirror}& other) : jvmSelf(other.jvmSelf), jvmSelfRef(other.jvmSelfRef), owners(other.owners) {")
        statement("(*owners)++")
        line("}")


        if (!jClass.isObject) jClass.constructors.forEach { func ->
            if (func.isExternal || func.isIgnoringJni) return@forEach
            val args = func.mirrorFuncArgs(env = true)?.joinToString(", ") ?: return@forEach
            line("${clMirror}::${clMirror}($args) {")

            val arguments = func.fullValueParameterList.joinToString("") { param ->
                val fieldTypeMirror = param.type.jniType() ?: return@joinToString ""
                ",\n " + fieldTypeMirror.transformToJni.invoke("${param.name}")
            }
            statement("owners = std::make_shared<int>(1)")
            statement("JNIEnv* env = ${BROOKLYN}::env()")
            statement(
                "${clMirror}::jvmSelf = env->NewObject( " +
                        "${mappingNamespace}::${indexField}->cls, " +
                        "${mappingNamespace}::${indexField}->${func.cppNameMirror} " +
                        "$arguments )"
            )
            statement("${clMirror}::jvmSelfRef = env->NewGlobalRef(jvmSelf)")
            line("}")
        }

        jClass.functions.forEach { func ->
            if (func.isExternal || func.isIgnoringJni) return@forEach
            val argsDeclaration = func.mirrorFuncArgs()?.joinToString(", ") ?: return@forEach
            val returnType = func.returnType.jniType()
            val arguments = func.fullValueParameterList.joinToString(",\n ") { param ->
                val fieldTypeMirror = param.type.jniType() ?: return@joinToString ""
                fieldTypeMirror.transformToJni.invoke("${param.name}")
            }

            when {
                jClass.isObject && returnType != null -> {
                    line("${returnType.cppFullTypeMirror} ${clMirror}::${func.name}($argsDeclaration) {")
                    statement("JNIEnv* env = ${BROOKLYN}::env()")
                    post("return ")
                    statement(
                        returnType.transformToCpp.invoke(
                            returnType.extractFromStaticMethod.invoke(
                                type = returnType,
                                jvmObj = "${mappingNamespace}::${indexField}->cls",
                                fieldOrMethodId = "${mappingNamespace}::${indexField}->${func.cppNameMirror} ",
                                args = arguments
                            )
                        )
                    )
                    line("}")
                }

                jClass.isObject -> {
                    line("void ${clMirror}::${func.name}($argsDeclaration) {")
                    statement("JNIEnv* env = ${BROOKLYN}::env()")
                    statement(
                        "env->CallStaticVoidMethod(${
                            listOf(
                                "${mappingNamespace}::${indexField}->cls",
                                "${mappingNamespace}::${indexField}->${func.cppNameMirror}",
                                arguments
                            ).joinArgs()
                        })"
                    )
                    line("}")
                }

                returnType != null -> {
                    line("${returnType.cppFullTypeMirror} ${clMirror}::${func.name}($argsDeclaration) {")
                    statement("JNIEnv* env = ${BROOKLYN}::env()")
                    post("return ")
                    statement(
                        returnType.transformToCpp.invoke(
                            returnType.extractFromMethod.invoke(
                                type = returnType,
                                jvmObj = "jvmSelf",
                                fieldOrMethodId = "${mappingNamespace}::${indexField}->${func.cppNameMirror} ",
                                args = arguments
                            )
                        )
                    )
                    line("}")
                }

                else -> {
                    line("void ${clMirror}::${func.name}($argsDeclaration) {")
                    statement("JNIEnv* env = ${BROOKLYN}::env()")
                    statement(
                        "env->CallVoidMethod(${
                            listOf(
                                "jvmSelf",
                                "${mappingNamespace}::${indexField}->${func.cppNameMirror}",
                                arguments
                            ).joinArgs()
                        })"
                    )
                    line("}")
                }
            }
        }


        jClass.properties.forEach { prop ->
            if (prop.isExternal || prop.isIgnoringJni) return@forEach
            val type = prop.jniType() ?: return@forEach

            val extractMethod = if (jClass.isObject) {
                type.extractFromStaticMethod
            } else {
                type.extractFromMethod
            }
            val callMethod = if (jClass.isObject) "CallStaticVoidMethod" else "CallVoidMethod"
            val selfArg = if (jClass.isObject) "${mappingNamespace}::${indexField}->cls" else "jvmSelf"

            line("${type.cppFullTypeMirror} ${clMirror}::get${prop.nameUpperCase}() {")
            statement("JNIEnv* env = ${BROOKLYN}::env()")
            post("return ")
            statement(
                type.transformToCpp.invoke(
                    extractMethod.invoke(
                        type = type,
                        jvmObj = selfArg,
                        fieldOrMethodId = "${mappingNamespace}::${indexField}->${prop.name}_getter",
                        args = ""
                    )
                )
            )
            line("}")

            if (!prop.isVar) return@forEach

            line("void ${clMirror}::set${prop.nameUpperCase}( const ${type.cppFullTypeMirror}& value ) {")
            statement("JNIEnv* env = ${BROOKLYN}::env()")

            statement(
                "env->${callMethod}(${
                    listOf(
                        selfArg,
                        "${mappingNamespace}::${indexField}->${prop.name}_setter",
                        type.transformToJni.invoke("value")
                    ).joinArgs()
                })"
            )
            line("}")
        }

        line("${clMirror}::~${clMirror}() {")
        statement("(*owners)--")
        line("if (*owners <= 0 && jvmSelfRef) {")
        statement(" ${BROOKLYN}::env()->DeleteGlobalRef(jvmSelfRef)")
        statement("jvmSelfRef = NULL")
        statement("jvmSelf = NULL")
        line("}")
        line("}")
    }


    header {
        usedTypes.mapNotNull { type ->
            type.jniType()?.classId
        }.toSet().forEach { classId ->
            include(classId.modelHeaderFile.path)
        }
    }
}


