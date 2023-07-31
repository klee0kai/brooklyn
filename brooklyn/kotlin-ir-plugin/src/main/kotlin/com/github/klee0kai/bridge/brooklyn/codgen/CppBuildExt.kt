package com.github.klee0kai.bridge.brooklyn.codgen

fun StringBuilder.headers(unicHeaderName: String, body: String): StringBuilder =
    apply {
        include("<jni.h>")
        include("<string>")
        include("<android/log.h>")
        include("<ostream>")
        include("<list>")

        line("#ifndef $unicHeaderName")
        line("#define $unicHeaderName")

        append(body)

        line("#endif //$unicHeaderName")
    }


fun StringBuilder.include(lib: String): StringBuilder = apply {
    append("#include ${lib}\n")
}

fun StringBuilder.line(code: String): StringBuilder = apply {
    append("${code}\n")
}
