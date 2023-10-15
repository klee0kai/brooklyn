#include <jni.h>
#include <string>
#include "brooklyn.h"

using namespace brooklyn;

std::shared_ptr<ComKlee0kaiExampleMirrorsSimpleJniMirror> simpleMirror = {};

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

ComKlee0kaiExampleMirrorsSimpleJniMirror ComKlee0kaiExampleEngineSimpleJniEngine::createSimpleMirror1() {
    return ComKlee0kaiExampleMirrorsSimpleJniMirror(41);
}

ComKlee0kaiExampleMirrorsSimpleJniMirror ComKlee0kaiExampleEngineSimpleJniEngine::createSimpleMirror2() {
    auto simpleMirror = ComKlee0kaiExampleMirrorsSimpleJniMirror("created from c++");
    //transfer of possession
    auto mirror2 = simpleMirror;
    auto mirror3 = ComKlee0kaiExampleMirrorsSimpleJniMirror(mirror2);
    return mirror3;
}

std::vector<ComKlee0kaiExampleModelSimple>
ComKlee0kaiExampleEngineSimpleJniEngine::copyArray(const std::vector<ComKlee0kaiExampleModelSimple> &simpleArray) {
    auto simpleArray2 = std::vector<ComKlee0kaiExampleModelSimple>(simpleArray.size());
    std::copy(simpleArray.begin(), simpleArray.end(), simpleArray2.begin());
    return simpleArray2;
}

std::shared_ptr<std::vector<ComKlee0kaiExampleModelSimple>> ComKlee0kaiExampleEngineSimpleJniEngine::copyArray2(
        const std::shared_ptr<std::vector<ComKlee0kaiExampleModelSimple>> &simpleArray) {
    return simpleArray;
}

std::vector<std::shared_ptr<ComKlee0kaiExampleModelSimple>> ComKlee0kaiExampleEngineSimpleJniEngine::copyArray3(
        const std::vector<std::shared_ptr<ComKlee0kaiExampleModelSimple>> &simpleArray) {
    return simpleArray;
}

std::shared_ptr<std::vector<std::shared_ptr<ComKlee0kaiExampleModelSimple>>>
ComKlee0kaiExampleEngineSimpleJniEngine::copyArray4(
        const std::shared_ptr<std::vector<std::shared_ptr<ComKlee0kaiExampleModelSimple>>> &simpleArray) {
    return simpleArray;
}


std::vector<ComKlee0kaiExampleMirrorsSimpleJniMirror> ComKlee0kaiExampleEngineSimpleJniEngine::copyMirrorArray(
        const std::vector<ComKlee0kaiExampleMirrorsSimpleJniMirror> &simpleArray) {
    return simpleArray;
}

std::shared_ptr<std::vector<ComKlee0kaiExampleMirrorsSimpleJniMirror>>
ComKlee0kaiExampleEngineSimpleJniEngine::copyMirrorArray2(
        const std::shared_ptr<std::vector<ComKlee0kaiExampleMirrorsSimpleJniMirror>> &simpleArray) {
    return simpleArray;
}

std::vector<std::shared_ptr<ComKlee0kaiExampleMirrorsSimpleJniMirror>>
ComKlee0kaiExampleEngineSimpleJniEngine::copyMirrorArray3(
        const std::vector<std::shared_ptr<ComKlee0kaiExampleMirrorsSimpleJniMirror>> &simpleArray) {
    return simpleArray;
}

std::shared_ptr<std::vector<std::shared_ptr<ComKlee0kaiExampleMirrorsSimpleJniMirror>>>
ComKlee0kaiExampleEngineSimpleJniEngine::copyMirrorArray4(
        const std::shared_ptr<std::vector<std::shared_ptr<ComKlee0kaiExampleMirrorsSimpleJniMirror>>> &simpleArray) {
    return simpleArray;
}


ComKlee0kaiExampleModelArraysModel
ComKlee0kaiExampleEngineSimpleJniEngine::copyArrayModel(const brooklyn::ComKlee0kaiExampleModelArraysModel &arrays) {
    return arrays;
}

void ComKlee0kaiExampleEngineSimpleJniEngine::holdSimpleMirror(
        const brooklyn::ComKlee0kaiExampleMirrorsSimpleJniMirror &simple) {
    simpleMirror = std::make_shared<ComKlee0kaiExampleMirrorsSimpleJniMirror>(simple);
}

void ComKlee0kaiExampleEngineSimpleJniEngine::unHoldSimpleMirror() {
    simpleMirror.reset();
}

void ComKlee0kaiExampleMirrorsSimpleJniMirror::incInCpp() {
    inc();
}

int ComKlee0kaiExampleMirrorsSimpleJniMirror::incInCpp2() {
    update(2, "from C++");
    return 1;
}

int ComKlee0kaiExampleMirrorsSimpleJniMirror::updateInCpp(const int &delta, const std::string &strDelta) {
    return 2;
}

int ComKlee0kaiExampleMirrorsSimpleJniMirror::objId() {
    return ComKlee0kaiExampleMirrorsSimpleJniMirror::obJvmId();
}