package com.github.klee0kai.bridge.brooklyn.cpp.model

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.fields
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.declareClassModelStructure(jClass: IrClass) = apply {
    variables {

        lines(1)
        line("struct ${jClass.jniType()?.cppTypeMirrorStr ?: return@variables} {")
        jClass.fields.forEach { field ->
            field.type.jniType()?.cppTypeMirrorStr?.let { cppType ->
                statement("\t${cppType} ${field.name};")
            }
        }
        jClass.properties.forEach { property ->
            val type = property.getter!!.returnType
            type.jniType()?.cppTypeMirrorStr?.let { cppType ->
                statement("\t${cppType} ${property.name}")
            }
        }
        line("};")

    }
}

fun IrClass.cppMappingNameSpace() = "${jniType()?.cppTypeMirrorStr}_mapping"
