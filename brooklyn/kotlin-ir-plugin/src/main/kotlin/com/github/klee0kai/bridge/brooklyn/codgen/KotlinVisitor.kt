package com.github.klee0kai.bridge.brooklyn.codgen

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.packageFqName
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid


class KotlinVisitor(
    val gen: FileBuildersCollection
) : IrElementVisitorVoid {

    override fun visitElement(element: IrElement) = element.acceptChildrenVoid(this)


    override fun visitFunction(func: IrFunction) {
        super.visitFunction(func)
        if (!func.isExternal) return
        val creator = func.headerCreator() ?: return


    }


    private fun IrDeclaration.headerCreator(): StringBuilder? =
        parentClassOrNull?.let { cl ->
            gen.getOrCreate(cl.packageFqName.toString(), cl.name.toString())
        }


}