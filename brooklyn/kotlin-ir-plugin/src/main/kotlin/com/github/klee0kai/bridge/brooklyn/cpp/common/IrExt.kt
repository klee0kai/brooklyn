package com.github.klee0kai.bridge.brooklyn.cpp.common

import com.github.klee0kai.bridge.brooklyn.JniMirror
import com.github.klee0kai.bridge.brooklyn.JniPojo
import com.github.klee0kai.bridge.brooklyn.cpp.typemirros.jniType
import com.github.klee0kai.bridge.brooklyn.di.DI
import org.jetbrains.kotlin.backend.jvm.codegen.AnnotationCodegen.Companion.annotationClass
import org.jetbrains.kotlin.backend.jvm.fullValueParameterList
import org.jetbrains.kotlin.descriptors.runtime.structure.classId
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.IdSignature
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.hasEqualFqName
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

val config get() = DI.config()

val ClassId.croppedPackageName: String
    get() {
        val group = config.group
        val pkg = packageFqName.toString()
        return if (!group.isNullOrBlank() && pkg.startsWith(group!!)) {
            pkg.substring(group.length)
        } else {
            pkg
        }
    }


val ClassId.fullClassName
    get() = "${packageFqName}.${shortClassName}"

val ClassId.croppedFullName
    get() = "${croppedPackageName}${shortClassName}"


val IrFunction.isConstructor
    get() = name.toString() == "<init>"

fun IrFunction.mirrorFuncArgs(env: Boolean = false) = runCatching {
    fullValueParameterList.map {
        "const ${it.type.jniType()!!.cppFullTypeMirror}& ${it.name}"
    }
}.getOrNull()


val IrDeclaration.isBrooklynPojo: Boolean
    get() = annotations.any { it.annotationClass.classId == JniPojo::class.java.classId }

val IrDeclaration.isBrooklynMirror: Boolean
    get() = annotations.any { it.annotationClass.classId == JniMirror::class.java.classId }

val IrModuleFragment.brooklynSrcFiles
    get() = files.filter { irFile: IrFile ->
        irFile.declarations.any { irDeclaration ->
            irDeclaration.isBrooklynPojo || irDeclaration.isBrooklynMirror
        }
    }


fun IrFunction.allUsedTypes(): List<IrType> =
    listOf(returnType) + fullValueParameterList.map { it.type }


fun IrType.jniTypeStr() =
    when {
        isUnit() -> "void"
        else -> jniType()?.jniTypeStr ?: "jobject"
    }


fun IrType.isNullableArray(argTypeCheck: (IrType) -> Boolean): Boolean {
    return if (isNullableArray()) {
        val argType = (this as? IrSimpleType)
            ?.arguments
            ?.getOrNull(0)
            ?.typeOrNull
            ?: return false

        return argTypeCheck.invoke(argType)
    } else false
}


fun IrType.isArray(argTypeCheck: (IrType) -> Boolean): Boolean {
    return if (isArray()) {
        val argType = (this as? IrSimpleType)
            ?.arguments
            ?.getOrNull(0)
            ?.typeOrNull
            ?: return false
        return argTypeCheck.invoke(argType)
    } else false
}


fun IrType.isClassType(signature: IdSignature.CommonSignature, nullable: Boolean? = null): Boolean {
    if (this !is IrSimpleType) return false
    if (nullable != null && this.isMarkedNullable() != nullable) return false
    return signature == classifier.signature ||
            classifier.owner.let { it is IrClass && it.hasFqNameEqualToSignature(signature) }
}

val IrMutableAnnotationContainer.isIgnoringJni
    get() = annotations.any {
        it.type.classFqName.toString() == "com.github.klee0kai.bridge.brooklyn.JniIgnore"
    }

private fun IrClass.hasFqNameEqualToSignature(signature: IdSignature.CommonSignature): Boolean =
    name.asString() == signature.shortName &&
            hasEqualFqName(FqName("${signature.packageFqName}.${signature.declarationFqName}"))