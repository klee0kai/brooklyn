plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.github.gmazzo.buildconfig")
    id("com.gradle.plugin-publish") version "1.0.0"
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
}

buildConfig {
    val project = project(":brooklyn-plugin")
    packageName(project.group.toString())
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.extra["name"]}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${project.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${project.name}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${project.version}\"")
}

gradlePlugin {
    plugins {
        create("brooklyn") {
            id = rootProject.extra["name"].toString()
            displayName = rootProject.extra["displayName"].toString()
            description = rootProject.extra["description"].toString()
            implementationClass = "com.github.klee0kai.bridge.brooklyn.BrooklynGradlePlugin"
        }
    }
}
