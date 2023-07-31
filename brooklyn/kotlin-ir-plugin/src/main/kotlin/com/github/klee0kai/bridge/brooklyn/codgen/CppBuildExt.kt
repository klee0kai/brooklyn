package com.github.klee0kai.bridge.brooklyn.codgen

fun StringBuilder.headers(unicHeaderName: String, body: String): StringBuilder =
    apply {
        include("<jni.h>")
        include("<string>")
        include("<android/log.h>")
        include("<ostream>")
        include("<list>")
        lines(1)

        line("#ifndef $unicHeaderName")
        line("#define $unicHeaderName")
        lines(2)

        append(body)

        lines(2)
        line("#endif //$unicHeaderName")
    }


fun StringBuilder.include(lib: String): StringBuilder = apply {
    append("#include ${lib}\n")
}

fun StringBuilder.line(code: String): StringBuilder = apply {
    append("${code}\n")
}

fun StringBuilder.lines(count: Int): StringBuilder = apply {
    repeat(count) { append("\n") }
}