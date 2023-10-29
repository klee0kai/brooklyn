package com.github.klee0kai.bridge.brooklyn.codegen

import com.github.klee0kai.bridge.brooklyn.cpp.common.isBrooklynMirror
import com.github.klee0kai.bridge.brooklyn.cpp.common.isBrooklynPojo
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedMirrorClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedPojoClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.allCppTypeMirrors
import com.github.klee0kai.bridge.brooklyn.di.DI
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid


class BrooklynTypes : IrElementVisitorVoid {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val project by lazy { DI.project() }

    private val cacheController by DI.cacheControllerLazy()

    val pojoJniClasses = mutableListOf<IrClass>()

    val mirrorJniClasses = mutableListOf<IrClass>()

    var nonCachedPojoJniClasses = listOf<IrClass>()
        private set
    var nonCachedMirrorJniClasses = listOf<IrClass>()
        private set

    suspend fun process() = withContext(defDispatcher) {
        project.files.forEach { file ->
            file.acceptVoid(this@BrooklynTypes)
        }

        pojoJniClasses.forEach {
            allCppTypeMirrors.addSupportedPojoClass(it)
        }
        mirrorJniClasses.forEach {
            allCppTypeMirrors.addSupportedMirrorClass(it)
        }

        nonCachedPojoJniClasses = pojoJniClasses.filter { irClass ->
            !cacheController.isCached(irClass.file.path)
        }
        nonCachedMirrorJniClasses = mirrorJniClasses.filter { irClass ->
            !cacheController.isCached(irClass.file.path)
        }
    }


    override fun visitElement(element: IrElement) = element.acceptChildrenVoid(this)

    override fun visitClass(declaration: IrClass) {
        super.visitClass(declaration)
        when {
            declaration.isBrooklynPojo -> pojoJniClasses.add(declaration)
            declaration.isBrooklynMirror -> mirrorJniClasses.add(declaration)
        }
    }

}