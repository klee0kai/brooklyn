buildscript {
    extra["group"] = "com.github.klee0kai.brooklyn"
    extra["displayName"] = "Brooklyn Bridge"
    extra["description"] = "Compile plugin to generate C++ code for jni"
    extra["version"] = "0.0.5"
    extra["site"] = "https://github.com/klee0kai/brooklyn_bridge"

}

allprojects {
    group = rootProject.extra["group"].toString()
    version = rootProject.extra["version"].toString()
}

