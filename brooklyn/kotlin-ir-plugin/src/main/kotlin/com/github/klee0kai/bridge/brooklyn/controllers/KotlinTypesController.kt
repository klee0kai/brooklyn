package com.github.klee0kai.bridge.brooklyn.controllers

import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedMirrorClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.addSupportedPojoClass
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.allCppTypeMirrors
import com.github.klee0kai.bridge.brooklyn.di.DI
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class KotlinTypesController {

    private val defDispatcher = DI.dispatchersModule().defaultDispatcher()

    private val headerCreator by DI.kotlinVisitorLazy()

    private val project by lazy { DI.project() }

    suspend fun process() = withContext(defDispatcher) {
        project.files.forEach { file ->
            file.acceptVoid(headerCreator)
        }

        headerCreator.pojoJniClasses.forEach {
            allCppTypeMirrors.addSupportedPojoClass(it)
        }
        headerCreator.mirrorJniClasses.forEach {
            allCppTypeMirrors.addSupportedMirrorClass(it)
        }
    }

}