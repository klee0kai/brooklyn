package com.github.klee0kai.bridge.brooklyn

import com.github.klee0kai.bridge.brooklyn.di.DI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class BrooklynIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val outDirFile: String,
    private val cachePath: String,
) : IrGenerationExtension {

    private val cacheController by lazy { DI.controllerModule().cacheController() }
    private val kotlinTypeController by lazy { DI.controllerModule().kotlinTypesController() }
    private val commonGenController by lazy { DI.controllerModule().commonGenController() }
    private val indexGenController by lazy { DI.controllerModule().indexGenController() }
    private val mappingGenController by lazy { DI.controllerModule().mapperGenController() }
    private val mirrorGenController by lazy { DI.controllerModule().mirrorGenController() }
    private val modelGenController by lazy { DI.controllerModule().modelGenController() }
    private val cmakeGenController by lazy { DI.controllerModule().cmakeGenController() }

    private val gen by DI.cppBuilderLazy()

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext): Unit = runBlocking {
        DI.context(pluginContext)
        DI.project(moduleFragment)

        // calc source changes
        cacheController.calcDiff()

        // collect support types
        kotlinTypeController.process()

        // create
        launch {
            launch { commonGenController.gen() }
            launch { indexGenController.gen() }
            launch { modelGenController.gen() }
            launch { mappingGenController.gen() }
            launch { mirrorGenController.gen() }
        }.join()

        // generate all files
        gen.genAll()

        // generate cmake file
        cmakeGenController.gen()

        // save fingerprint
        cacheController.saveFingerPrint()

    }

}