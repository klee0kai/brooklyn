package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.JniPojo
import com.github.klee0kai.bridge.brooklyn.cpp.*
import org.jetbrains.kotlin.backend.jvm.codegen.AnnotationCodegen.Companion.annotationClass
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid


class KotlinVisitor(val gen: CppBuildersCollection) : IrElementVisitorVoid {

    private val fileInitBlock: CodeBuilder.() -> Unit = {
        defHeaders()
        namespaces("brooklyn", "mapper")
    }

    override fun visitElement(element: IrElement) = element.acceptChildrenVoid(this)

    override fun visitClass(declaration: IrClass) {
        super.visitClass(declaration)
        val isJniPojo = declaration.annotations
            .any { it.annotationClass.classId == JniPojo::class.java.classId }
        val clId = declaration.classId!!
        when {
            isJniPojo -> {
                gen.getOrCreate(clId.mapperHeaderFile, fileInitBlock)
                    .initJniClassApi(declaration)
                    .deinitJniClassApi(declaration)


                gen.getOrCreate(clId.mapperCppFile, fileInitBlock)
                    .header { include(clId.mapperHeaderFile.path) }
                    .declareClassIndexStructure(declaration)
                    .initJniClassImpl(declaration)
                    .deinitJniClassImpl(declaration)
            }
        }
    }

    override fun visitFunction(func: IrFunction) {
        super.visitFunction(func)
        if (!func.isExternal) return
        val creator = gen.getOrCreate(func.parentAsClass.classId!!.structuresHeaderFile)
    }


}