buildscript {
    extra["group"] = "com.github.klee0kai.bridge.brooklyn"
    extra["displayName"] = "Brooklyn Bridge"
    extra["description"] = "Compile plugin to generate C++ code for jni"
    extra["version"] = "0.0.1"
    extra["site"] = "https://github.com/klee0kai/brooklyn_bridge"

}

plugins {
    kotlin("jvm") version "1.7.10" apply false
    id("org.jetbrains.dokka") version "1.7.10" apply false
    id("com.github.gmazzo.buildconfig") version "3.1.0" apply false
}

allprojects {
    group = rootProject.extra["group"].toString()
    version = rootProject.extra["version"].toString()
}

subprojects {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
