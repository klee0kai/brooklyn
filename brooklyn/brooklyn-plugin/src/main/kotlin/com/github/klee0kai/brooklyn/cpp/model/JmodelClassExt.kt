package com.github.klee0kai.brooklyn.cpp.model

import com.github.klee0kai.brooklyn.cpp.typemirros.jniType
import com.github.klee0kai.brooklyn.cpp.common.*
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.properties

fun CodeBuilder.declareClassModelStructure(jClass: IrClass) = apply {
    val usedTypes = mutableSetOf<IrType>()
    variables {
        lines(1)
        line("struct ${jClass.jniType()?.cppSimpleTypeMirrorStr ?: return@variables} {")
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

