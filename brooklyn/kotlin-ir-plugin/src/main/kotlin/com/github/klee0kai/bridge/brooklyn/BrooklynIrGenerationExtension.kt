package com.github.klee0kai.bridge.brooklyn

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.isPropertyAccessor
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.types.isNullableString
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.isGetter
import org.jetbrains.kotlin.ir.visitors.*

class BrooklynIrGenerationExtension(
    private val messageCollector: MessageCollector,
    private val outDirFile: String
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val transformer = AccessorCallTransformer(pluginContext, outDirFile)
        moduleFragment.files.forEach { file ->
            transformer.runOnFileInOrder(file)
        }
    }

    internal class AccessorCallTransformer(
        private val context: IrPluginContext,
        private val text: String
    ) : IrElementTransformerVoidWithContext(), FileLoweringPass {

        override fun lower(irFile: IrFile) {
            irFile.transformChildrenVoid()
        }

        override fun visitFunctionNew(declaration: IrFunction): IrStatement {
            return if (declaration.isPropertyAccessor && declaration.isGetter && (declaration.returnType.isString() || declaration.returnType.isNullableString())
            ) {
                declaration.body?.transformChildrenVoid(object : IrElementTransformerVoid() {
                    override fun visitReturn(expression: IrReturn): IrExpression {
                        return IrBlockBuilder(
                            context,
                            currentScope?.scope!!,
                            expression.startOffset,
                            expression.endOffset
                        ).irBlock {
                            val irConcat = irConcat()
                            irConcat.addArgument(irString("${text} "))
                            irConcat.addArgument(expression.value)
                            +irReturn(irConcat)
                        }
                    }
                })
                super.visitFunctionNew(declaration)
            } else {
                super.visitFunctionNew(declaration)
            }
        }

    }
}


fun FileLoweringPass.runOnFileInOrder(irFile: IrFile) {
    irFile.acceptVoid(object : IrElementVisitorVoid {
        override fun visitElement(element: IrElement) {
            element.acceptChildrenVoid(this)
        }

        override fun visitFile(declaration: IrFile) {
            lower(declaration)
            declaration.acceptChildrenVoid(this)
        }
    })
}
