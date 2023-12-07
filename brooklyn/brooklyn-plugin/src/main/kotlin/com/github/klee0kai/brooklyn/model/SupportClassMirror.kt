package com.github.klee0kai.brooklyn.model

import com.github.klee0kai.brooklyn.cpp.typemirros.cppMappingNameSpace
import com.github.klee0kai.brooklyn.cpp.typemirros.cppModelMirror
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.ClassId

@Serializable
data class SupportClassMirror(
    val classIdString: String,
    val cppModelMirror: String,
    val namespace: String,
    val kotlinFqName: String,
    val orFilePath: String,
) {

    val classId: ClassId get() = ClassId.fromString(classIdString)

}


fun IrClass.toSupportClassMirror() = SupportClassMirror(
    classIdString = classId!!.asString(),
    cppModelMirror = cppModelMirror()!!,
    namespace = cppMappingNameSpace(),
    kotlinFqName = kotlinFqName.toString(),
    orFilePath = file.path
)