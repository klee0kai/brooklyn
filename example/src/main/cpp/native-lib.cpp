#include <jni.h>
#include <string>
#include "brooklyn.h"

using namespace brooklyn;

std::shared_ptr<MirrorsSimpleJniMirror> simpleMirror = {};

ModelSimple
brooklyn::EngineSimpleJniEngine::copySimple(const brooklyn::ModelSimple &simple) {
    auto simple2 = simple;
    simple2.age++;
    simple2.address += "from c++";
    *simple2.name += "from c++";
    return simple2;
}

ModelNullableTypePojo brooklyn::EngineSimpleJniEngine::copyNullableType(
        const ModelNullableTypePojo &simple) {
    return simple;
}

MirrorsSimpleJniMirror EngineSimpleJniEngine::createSimpleMirror1() {
    return MirrorsSimpleJniMirror(41);
}

MirrorsSimpleJniMirror EngineSimpleJniEngine::createSimpleMirror2() {
    auto simpleMirror = MirrorsSimpleJniMirror("created from c++");
    //transfer of possession
    auto mirror2 = simpleMirror;
    auto mirror3 = MirrorsSimpleJniMirror(mirror2);
    return mirror3;
}

std::vector<ModelSimple>
EngineSimpleJniEngine::copyArray(const std::vector<ModelSimple> &simpleArray) {
    auto simpleArray2 = std::vector<ModelSimple>(simpleArray.size());
    std::copy(simpleArray.begin(), simpleArray.end(), simpleArray2.begin());
    return simpleArray2;
}

std::shared_ptr<std::vector<ModelSimple>> EngineSimpleJniEngine::copyArray2(
        const std::shared_ptr<std::vector<ModelSimple>> &simpleArray) {
    return simpleArray;
}

std::vector<std::shared_ptr<ModelSimple>> EngineSimpleJniEngine::copyArray3(
        const std::vector<std::shared_ptr<ModelSimple>> &simpleArray) {
    return simpleArray;
}

std::shared_ptr<std::vector<std::shared_ptr<ModelSimple>>>
EngineSimpleJniEngine::copyArray4(
        const std::shared_ptr<std::vector<std::shared_ptr<ModelSimple>>> &simpleArray) {
    return simpleArray;
}


std::vector<MirrorsSimpleJniMirror> EngineSimpleJniEngine::copyMirrorArray(
        const std::vector<MirrorsSimpleJniMirror> &simpleArray) {
    return simpleArray;
}

std::shared_ptr<std::vector<MirrorsSimpleJniMirror>>
EngineSimpleJniEngine::copyMirrorArray2(
        const std::shared_ptr<std::vector<MirrorsSimpleJniMirror>> &simpleArray) {
    return simpleArray;
}

std::vector<std::shared_ptr<MirrorsSimpleJniMirror>>
EngineSimpleJniEngine::copyMirrorArray3(
        const std::vector<std::shared_ptr<MirrorsSimpleJniMirror>> &simpleArray) {
    return simpleArray;
}

std::shared_ptr<std::vector<std::shared_ptr<MirrorsSimpleJniMirror>>>
EngineSimpleJniEngine::copyMirrorArray4(
        const std::shared_ptr<std::vector<std::shared_ptr<MirrorsSimpleJniMirror>>> &simpleArray) {
    return simpleArray;
}


ModelArraysModel
EngineSimpleJniEngine::copyArrayModel(const ModelArraysModel &arrays) {
    return arrays;
}

ModelBoxedArraysModel EngineSimpleJniEngine::copyBoxedArrayModel(
        const ModelBoxedArraysModel &arrays) {
    return arrays;
}

void EngineSimpleJniEngine::holdSimpleMirror(
        const MirrorsSimpleJniMirror &simple) {
    simpleMirror = std::make_shared<MirrorsSimpleJniMirror>(simple);
}

void EngineSimpleJniEngine::unHoldSimpleMirror() {
    simpleMirror.reset();
}

void MirrorsSimpleJniMirror::incInCpp() {
    inc();
}

int MirrorsSimpleJniMirror::incInCpp2() {
    update(2, "from C++");
    return 1;
}

int MirrorsSimpleJniMirror::updateFromCpp(const int &delta, const std::string &strDelta) {
    update(delta, strDelta);
    return 1;
}

int MirrorsSimpleJniMirror::updateFromCppDirectly(const int &delta, const std::string &strDelta) {
    setSomeInt(getSomeInt() + delta);
    setSomeString(getSomeString() + strDelta);
    return 2;
}


int MirrorsSimpleJniMirror::objId() {
    return MirrorsSimpleJniMirror::getId();
}