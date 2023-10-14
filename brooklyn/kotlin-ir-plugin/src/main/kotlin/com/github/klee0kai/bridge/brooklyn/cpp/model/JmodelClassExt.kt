package com.github.klee0kai.bridge.brooklyn.cpp.model

import com.github.klee0kai.bridge.brooklyn.cpp.common.*
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.fields
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.declareClassModelStructure(jClass: IrClass) = apply {
    val usedTypes = mutableSetOf<IrType>()
    variables {
        lines(1)
        line("struct ${jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@variables} {")
        jClass.fields.forEach { field ->
            field.type.jniType()?.cppFullTypeMirror?.let { cppType ->
                statement("\t${cppType} ${field.name};")
            }
            usedTypes.add(field.type)
        }
        jClass.properties.forEach { property ->
            val type = property.getter!!.returnType
            type.jniType()?.cppFullTypeMirror?.let { cppType ->
                statement("\t${cppType} ${property.name}")
            }
            usedTypes.add(type)
        }
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

