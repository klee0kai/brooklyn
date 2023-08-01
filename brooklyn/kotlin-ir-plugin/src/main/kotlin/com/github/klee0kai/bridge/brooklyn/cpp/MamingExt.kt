package com.github.klee0kai.bridge.brooklyn.cpp


fun String.camelCase() = buildString {
    var specSymbol = false
    trimSpecSymbols().forEach {
        when {
            it in "._" -> specSymbol = true
            specSymbol -> {
                append(it.uppercaseChar())
                specSymbol = false
            }

            else -> append(it)
        }
    }
}


fun String.snakeCase() = buildString {
    var specSymbol = false
    trimSpecSymbols().forEach {
        when {
            it in "._" -> specSymbol = true
            specSymbol -> {
                append("_")
                append(it)
                specSymbol = false
            }

            else -> {
                append(it)
            }
        }
    }
}


private fun String.trimSpecSymbols() =
    this.replace(".", "_").filter { it !in "<>" }