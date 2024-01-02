//
// Created by panda on 02.01.24.
//

#include <jni.h>
#include <string>
#include "brooklyn.h"

using namespace brooklyn;

void InterfacemirrorsInterfacesEngine::callSimple(const brooklyn::InterfacemirrorsSimpleInterfaceCallback &callback) {
    ((InterfacemirrorsSimpleInterfaceCallback *) &callback)->inc();
}

void InterfacemirrorsInterfacesEngine::callFun(const brooklyn::InterfacemirrorsFunInterfaceCallback &callback) {
    ((InterfacemirrorsFunInterfaceCallback * ) & callback)->inc();
}

