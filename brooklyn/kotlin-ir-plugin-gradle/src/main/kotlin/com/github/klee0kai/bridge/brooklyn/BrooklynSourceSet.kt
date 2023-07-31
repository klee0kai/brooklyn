package com.github.klee0kai.bridge.brooklyn

import org.gradle.api.Describable
import org.gradle.api.Named
import org.gradle.api.model.ObjectFactory
import java.io.File
import javax.inject.Inject

abstract class BrooklynSourceSet @Inject constructor(
    @JvmField val name: String,
    val objectFactory: ObjectFactory,
) : Named, Describable {

    var enabled: Boolean = false

    var outDir: File? = null

    override fun getName(): String = this.name

    override fun getDisplayName(): String = this.name


}
