import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("java")
    kotlin("jvm")
    application
}

group = "org.template.term"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}