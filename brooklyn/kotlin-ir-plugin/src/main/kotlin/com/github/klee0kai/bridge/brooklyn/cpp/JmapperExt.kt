package com.github.klee0kai.bridge.brooklyn.cpp

import com.github.klee0kai.bridge.brooklyn.poet.Poet
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.ClassId

fun CodeBuilder.declareClassStructure(jClass: IrClass) = apply {
    variables.post(Poet().apply {
        line("static struct ${jClass.classId!!.indexStructName} {")
        jClass.constructors.forEach { func ->
            line("\tjmethodID ${func.cppNameMirror};")
        }
        jClass.fields.forEach { field ->
            line("\tjfieldID ${field.name};")
        }
        jClass.properties.forEach { property ->
            line("\tjmethodID ${property.name}_getter;")
            if (property.isVar) line("\tjmethodID ${property.name}_setter;")
        }
        jClass.functions.forEach { func ->
            line("\tjmethodID ${func.cppNameMirror};")
        }
        line("} *${jClass.classId!!.indexVariableName} = NULL;")
    })


}

val ClassId.indexStructName
    get() = "${packageFqName}${shortClassName}_index".camelCase()

val ClassId.indexVariableName
    get() = "${packageFqName}${shortClassName}Index".camelCase()

val IrFunction.cppNameMirror
    get() = "$name${fullValueParameterList.map { it.type to it.isVararg }.hashCode()}".camelCase()