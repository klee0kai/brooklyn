package com.klee0kai.tests.interfacemirrors

import com.github.klee0kai.brooklyn.JniMirror

@JniMirror
object InterfacesEngine {

    external fun callSimple(callback: SimpleInterfaceCallback)

    external fun callFun(callback: FunInterfaceCallback)

}