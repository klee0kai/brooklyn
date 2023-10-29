package com.github.klee0kai.bridge.brooklyn.cpp.mirror

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.common.CommonNaming.BROOKLYN
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.cppModelMirror
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.isObject

fun CodeBuilder.implMirrorInterface(jClass: IrClass) = apply {
    val usedTypes = mutableSetOf<IrType>()
    val clId = jClass.classId ?: return@apply

    jClass.functions.forEach { func ->
        if (!func.isExternal || func.isIgnoringJni) return@forEach
        usedTypes.addAll(func.allUsedTypes())

        val mappedArgs = func.fullValueParameterList.map { arg ->
            arg.type.jniType()?.transformToCpp?.invoke(arg.name.toString()) ?: "0"
        }
        val returnType = func.returnType.jniType()

        line("extern \"C\" JNIEXPORT ${func.returnType.jniTypeStr()} JNICALL")
        line(
            "Java_" +
                    "${clId.packageFqName.toString().snakeCase("_")}_" +
                    "${clId.shortClassName.toString().snakeCase("_")}_" +
                    "${func.name} ("
        )
        line("JNIEnv *env, ")
        if (jClass.isObject) {
            //static method
            line("jclass jClass")
        } else {
            line("jobject jObject")
        }
        func.fullValueParameterList.forEach { arg ->
            post(", ")
            line("${arg.type.jniTypeStr()} ${arg.name}")
        }
        line("){")
        statement("brooklyn::bindEnv(env)")

        when {
            jClass.isObject && returnType != null -> {
                post("auto jvmResultObj = ")
                statement(" ${BROOKLYN}::${jClass.cppModelMirror()}::${func.name} ( ${mappedArgs.joinToString(", ")} )")
                statement("return ${returnType.transformToJni.invoke("jvmResultObj")}")
            }

            jClass.isObject -> {
                statement(" ${BROOKLYN}::${jClass.cppModelMirror()}::${func.name} ( ${mappedArgs.joinToString(", ")} )")
            }

            returnType != null -> {
                statement("auto mirror = ${BROOKLYN}::${jClass.cppModelMirror()}(jObject)")
                statement("auto jvmResultObj = mirror.${func.name} ( ${mappedArgs.joinToString(", ")} )")
                statement("return ${returnType.transformToJni.invoke("jvmResultObj")}")
            }

            else -> {
                statement("auto mirror = ${BROOKLYN}::${jClass.cppModelMirror()}(jObject)")
                statement("mirror.${func.name} ( ${mappedArgs.joinToString(", ")} )")
            }
        }
        line("}")
    }


    header {
        usedTypes.mapNotNull { type ->
            type.jniType()?.classId
        }.toSet().forEach { classId ->
            include(classId.modelHeaderFile.path)
            include(classId.mapperHeaderFile.path)
        }
    }
}


