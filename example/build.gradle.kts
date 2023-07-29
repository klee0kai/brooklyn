plugins {
    id("java")
    kotlin("jvm")
    id("brooklyn")

}

brooklyn {
    enabled = true

    outDir = file("some")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

