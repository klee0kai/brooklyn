package com.github.klee0kai.bridge.brooklyn.codgen

import org.jetbrains.kotlin.incremental.mkdirsOrThrow
import java.io.File

class FileBuildersCollection(
    val outDir: File
) {

    private val builders = HashMap<File, StringBuilder>()

    fun getOrCreate(pkg: String, clName: String): StringBuilder {
        val pkgName = pkg.replace(".", "_").filter { it !in "<>" }
        val name = "${pkgName}_${clName}.h"
        val file = File(outDir, name)
        builders.putIfAbsent(file, StringBuilder())
        return builders[file]!!
    }

    fun genAll() {
        builders.forEach { file, builder ->
            file.parentFile.mkdirsOrThrow()
            file.writeBytes(
                StringBuilder()
                    .headers(file.name.uppercase(), builder.toString())
                    .toString()
                    .encodeToByteArray()
            )
        }
    }

}