package com.github.klee0kai.brooklyn.codegen

import com.github.klee0kai.brooklyn.cpp.common.isBrooklynMirror
import com.github.klee0kai.brooklyn.cpp.common.isBrooklynPojo
import com.github.klee0kai.brooklyn.cpp.typemirros.addSupportedMirrorClass
import com.github.klee0kai.brooklyn.cpp.typemirros.addSupportedPojoClass
import com.github.klee0kai.brooklyn.cpp.typemirros.allCppTypeMirrors
import com.github.klee0kai.brooklyn.di.DI
import com.github.klee0kai.brooklyn.model.SupportClassMirror
import com.github.klee0kai.brooklyn.model.toSupportClassMirror
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid


class BrooklynTypes : IrElementVisitorVoid {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val project by lazy { DI.project() }

    private val cacheStore by DI.cacheStoreLazy()

    val pojoJniClasses = mutableListOf<SupportClassMirror>()

    val mirrorJniClasses = mutableListOf<SupportClassMirror>()

    val nonCachedPojoJniClasses = mutableListOf<IrClass>()
    val nonCachedMirrorJniClasses = mutableListOf<IrClass>()

    suspend fun process() = withContext(defDispatcher) {
        project.files.forEach { file ->
            file.acceptVoid(this@BrooklynTypes)
        }
        val fingerPrint = cacheStore.currentFingerPrint
        val nonChangedPojoClasses = fingerPrint.supportPojoClasses.filter { cached ->
            pojoJniClasses.none { cached.classId == it.classId }
        }
        val nonChangedMirrorClasses = fingerPrint.supportMirrorClasses.filter { cached ->
            mirrorJniClasses.none { cached.classId == it.classId }
        }
        pojoJniClasses.addAll(nonChangedPojoClasses)
        mirrorJniClasses.addAll(nonChangedMirrorClasses)
        cacheStore.setSupportTypes(
            pojoJniClasses = pojoJniClasses,
            mirrorJniClasses = mirrorJniClasses
        )


        pojoJniClasses.forEach {
            allCppTypeMirrors.addSupportedPojoClass(it)
        }
        mirrorJniClasses.forEach {
            allCppTypeMirrors.addSupportedMirrorClass(it)
        }

    }


    override fun visitElement(element: IrElement) = element.acceptChildrenVoid(this)

    override fun visitClass(declaration: IrClass) {
        super.visitClass(declaration)
        when {
            declaration.isBrooklynPojo -> {
                nonCachedPojoJniClasses.add(declaration)
                pojoJniClasses.add(declaration.toSupportClassMirror())
            }

            declaration.isBrooklynMirror -> {
                nonCachedMirrorJniClasses.add(declaration)
                mirrorJniClasses.add(declaration.toSupportClassMirror())
            }
        }
    }

}