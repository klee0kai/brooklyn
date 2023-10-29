package com.github.klee0kai.bridge.brooklyn.cpp.mapper

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.declareClassIndexStructure(jClass: IrClass, pojo: Boolean = false) = apply {
    variables {
        val clId = jClass.classId!!
        lines(1)
        line("struct ${clId.indexStructName} {")
        statement("jclass cls")
        jClass.properties.forEach { property ->
            if (property.isIgnoringJni) return@forEach
            statement("\tjmethodID ${property.name}_getter = NULL")
            if (!pojo && property.isVar) statement("\tjmethodID ${property.name}_setter = NULL")
        }
        val methods = if (pojo) jClass.constructors else (jClass.constructors + jClass.functions)
        methods.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            statement("\tjmethodID ${func.cppNameMirror} = NULL")
        }
        statement("}")

        statement("extern std::shared_ptr<${clId.indexStructName}> ${clId.indexVariableName}")
    }
}

fun CodeBuilder.declareClassIndexField(jClass: IrClass) = apply {
    variables {
        val clId = jClass.classId!!
        statement("std::shared_ptr<${clId.indexStructName}> ${clId.indexVariableName} = {}")
    }
}

fun CodeBuilder.declareClassNamingStructure(jClass: IrClass, pojo: Boolean = false) = apply {
    variables {
        val clId = jClass.classId!!
        lines(1)
        line("struct ${clId.namingStructName} {")
        statement("const char * cls")
        jClass.properties.forEach { property ->
            if (property.isIgnoringJni) return@forEach
            statement("const char * ${property.name}_getter")
            if (!pojo && property.isVar) statement("const char * ${property.name}_setter")
        }
        val methods = if (pojo) jClass.constructors else (jClass.constructors + jClass.functions)
        methods.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            statement("const char * ${func.cppNameMirror}")
        }
        statement("}")

        statement("extern ${clId.namingStructName} ${clId.namingVariableName}")
    }
}

fun CodeBuilder.declareClassNamingField(jClass: IrClass, pojo: Boolean = false) = apply {
    variables {
        val clId = jClass.classId!!
        val clPathName = "${clId.packageFqName}/${clId.shortClassName}".snakeCase("/")
        comment("namingstructure: ${jClass.classId?.fullClassName};")

        val fieldInits = buildList<String> {
            add(".cls = \"${clPathName}\"")
            jClass.properties.forEach { property ->
                if (property.isIgnoringJni) return@forEach
                add(".${property.name}_getter = \"get${property.nameUpperCase}\"")
                if (!pojo && property.isVar) add(".${property.name}_setter = \"set${property.nameUpperCase}\"")
            }
            val methods = if (pojo) jClass.constructors else (jClass.constructors + jClass.functions)
            methods.forEach { func ->
                if (func.isIgnoringJni) return@forEach
                add(".${func.cppNameMirror} = \"${func.name}\"")
            }
        }

        line("${clId.namingStructName} ${clId.namingVariableName} = {")
        line(fieldInits.joinToString(",\n"))
        statement("}")
        comment("namingstructurefinish: ${jClass.classId?.fullClassName};")
    }
}


fun CodeBuilder.initJniClassApi() = apply {
    body {
        lines(1)
        statement("int init(JNIEnv *env)")
    }
}

fun CodeBuilder.initJniClassImpl(jClass: IrClass, pojo: Boolean = false) = apply {
    val usedTypes = mutableSetOf<IrType>()

    body {
        lines(1)
        val clId = jClass.classId!!
        line("int init(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) return 0")
        statement("${clId.indexVariableName} = std::make_shared<${clId.indexStructName}>()")
        statement("${clId.indexVariableName}->cls = (jclass) env->NewGlobalRef( env->FindClass(${clId.namingVariableName}.cls) )")
        jClass.properties.forEach { property ->
            if (property.isIgnoringJni) return@forEach

            val type = property.getter!!.returnType
            val jniTypeCode = type.jniType()?.jniTypeCode ?: return@forEach

            //getter
            post("${clId.indexVariableName}->${property.name}_getter = env->GetMethodID(${clId.indexVariableName}->cls, ${clId.namingVariableName}.${property.name}_getter, ")
            post("( std::string() + \"()${jniTypeCode}\" ).c_str()")
            statement(")")
            statement("if(!${clId.indexVariableName}->${property.name}_getter) return -1")
            //setter
            if (!pojo && property.isVar) {
                post("${clId.indexVariableName}->${property.name}_setter = env->GetMethodID(${clId.indexVariableName}->cls, ${clId.namingVariableName}.${property.name}_setter, ")
                post("( std::string() + \"(${jniTypeCode})V\" ).c_str()")
                statement(")")
                statement("if(!${clId.indexVariableName}->${property.name}_setter) return -1")
            }
        }
        val methods = if (pojo) jClass.constructors else (jClass.constructors + jClass.functions)
        methods.forEach { func ->
            if (func.isIgnoringJni) return@forEach
            usedTypes.addAll(func.allUsedTypes())
            val argTypes = runCatching {
                func.fullValueParameterList.joinToString("") { it.type.jniType()!!.jniTypeCode }
            }.getOrNull() ?: return@forEach
            val returnType = runCatching {
                if (func.isConstructor) "V" else func.returnType.jniType()!!.jniTypeCode
            }.getOrNull() ?: "V"


            post("${clId.indexVariableName}->${func.cppNameMirror} = env->GetMethodID(${clId.indexVariableName}->cls, ${clId.namingVariableName}.${func.cppNameMirror}, ")
            post("( std::string() + \"(${argTypes})${returnType}\" ).c_str() ")
            statement(")")
            statement("if(!${clId.indexVariableName}->${func.cppNameMirror}) return -1")
        }

        statement("return 0")
        line("}")
    }

    header {
        usedTypes.mapNotNull { type ->
            type.jniType()?.classId
        }.toSet().forEach { classId ->
            include(classId.mapperHeaderFile.path)
        }
    }
}

fun CodeBuilder.deinitJniClassApi() = apply {
    body {
        lines(1)
        statement("int deinit(JNIEnv *env)")
    }
}


fun CodeBuilder.deinitJniClassImpl(jClass: IrClass) = apply {
    body {
        val clId = jClass.classId!!
        lines(1)
        line("int deinit(JNIEnv *env) {")
        statement("if (${clId.indexVariableName}) env->DeleteGlobalRef(${clId.indexVariableName}->cls)")
        statement("${clId.indexVariableName}.reset()")
        statement("return 0")
        line("}")
    }
}

