package com.github.klee0kai.bridge.brooklyn.codgen

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import java.io.File


class CppHeaderCreater(
    private val outFile: File
) : IrElementVisitorVoid {

    private val body = StringBuilder()

    override fun visitElement(element: IrElement) = element.acceptChildrenVoid(this)

    override fun visitFunction(func: IrFunction) {
        super.visitFunction(func)
        print("func ${func}")
    }


    fun gen() {
        outFile.writeBytes(
            StringBuilder()
                .headers(outFile.name.uppercase(), body.toString())
                .toString()
                .encodeToByteArray()
        )
    }


}