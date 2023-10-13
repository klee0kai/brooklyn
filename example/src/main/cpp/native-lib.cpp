#include <jni.h>
#include <string>
#include "brooklyn.h"

using namespace brooklyn;

ComKlee0kaiExampleModelSimple
brooklyn::ComKlee0kaiExampleEngineSimpleJniEngine::copySimple(const brooklyn::ComKlee0kaiExampleModelSimple &simple) {
    auto simple2 = simple;
    simple2.age++;
    simple2.address += "from c++";
    *simple2.name += "from c++";
    return simple2;
}

ComKlee0kaiExampleModelNullableTypePojo brooklyn::ComKlee0kaiExampleEngineSimpleJniEngine::copyNullableType(
        const brooklyn::ComKlee0kaiExampleModelNullableTypePojo &simple) {
    return simple;
}

void ComKlee0kaiExampleMirrorsSimpleJniMirror::incToCpp() {
    inc();
}
