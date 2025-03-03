plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildconfig)
    kotlin("kapt")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.0.0"
}

val brooklynPluginName = "brooklyn-plugin"
group = rootProject.extra["group"].toString()
version = rootProject.extra["version"].toString()


dependencies {
    implementation(kotlin("gradle-plugin-api"))

    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    implementation("${group}:annotations:${version}")

    kapt("com.google.auto.service:auto-service:1.0.1")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0.1")

    implementation("com.github.klee0kai.stone:kotlin_lib:1.0.3")
    kapt("com.github.klee0kai.stone:stone_processor:1.0.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-jvm:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")


    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.5.0")
}

buildConfig {
    val project = project(":brooklyn-plugin")
    packageName(project.group.toString())
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${brooklynPluginName}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${rootProject.extra["group"].toString()}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${project.name}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${rootProject.extra["version"].toString()}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_SITE", "\"${rootProject.extra["site"]}\"")
}

gradlePlugin {
    plugins.register(brooklynPluginName) {
        id = brooklynPluginName
        group = rootProject.extra["group"].toString()
        version = rootProject.extra["version"].toString()
        displayName = rootProject.extra["displayName"].toString()
        description = rootProject.extra["description"].toString()
        implementationClass = "com.github.klee0kai.brooklyn.BrooklynGradlePlugin"
    }
}