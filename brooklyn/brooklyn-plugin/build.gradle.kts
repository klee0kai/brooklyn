plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kapt)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.publish.plugin)
}

val brooklynPluginName = "brooklyn-plugin"
group = rootProject.extra["group"].toString()
version = rootProject.extra["version"].toString()


dependencies {
    implementation(kotlin("gradle-plugin-api"))

    compileOnly(libs.kotlin.compiler)
    implementation("${group}:annotations:${version}")

    kapt(libs.auto.service.kapt)
    compileOnly(libs.auto.service.annotations)

    implementation(libs.stone.kotlin)
    kapt(libs.stone.kapt)

    implementation(libs.kotlinx.protobuf.jvm)
    implementation(libs.android.coroutines)

    testImplementation(libs.kotlin.junit)
    testImplementation(libs.compiletesting)
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