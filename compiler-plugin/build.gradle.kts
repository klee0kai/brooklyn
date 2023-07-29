plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("java-gradle-plugin")
}

val ktVersion = "1.7.0"

gradlePlugin {
    plugins {
        create("brooklyn.bridge") {
            id = "brooklyn.bridge"
            implementationClass = "com.github.klee0kai.bridge.brooklyn.BrooklynBridgePlugin"
        }
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$ktVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$ktVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$ktVersion")

    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.0-rc5")
}

