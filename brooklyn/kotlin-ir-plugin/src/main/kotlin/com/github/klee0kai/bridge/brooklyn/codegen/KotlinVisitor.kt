package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.JniMirror
import com.github.klee0kai.bridge.brooklyn.JniPojo
import org.jetbrains.kotlin.backend.jvm.codegen.AnnotationCodegen.Companion.annotationClass
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid


class KotlinVisitor : IrElementVisitorVoid {

    val pojoJniClasses = mutableListOf<IrClass>()
    val mirrorJniClasses = mutableListOf<IrClass>()


    override fun visitElement(element: IrElement) = element.acceptChildrenVoid(this)

    override fun visitClass(declaration: IrClass) {
        super.visitClass(declaration)
        val isJniPojo = declaration.annotations
            .any { it.annotationClass.classId == JniPojo::class.java.classId }
        val isJniMirror = declaration.annotations
            .any { it.annotationClass.classId == JniMirror::class.java.classId }
        when {
            isJniPojo -> pojoJniClasses.add(declaration)
            isJniMirror -> mirrorJniClasses.add(declaration)
        }
    }

    override fun visitFunction(func: IrFunction) {
        super.visitFunction(func)
        if (!func.isExternal) return
    }


}