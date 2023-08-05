import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    kotlin("jvm")
    id("brooklyn")
    id("klee0kai-crosscompile")
    id("com.github.gmazzo.buildconfig") version ("3.1.0")
}

sourceSets {
    main {
        brooklyn {
            enabled = true
        }
    }
}

buildConfig {
    buildConfigField("String", "NATIVE_LIB_PATH", "\"${File(buildDir, "libs/libnative_lib.so").absolutePath}\"")
}

crosscompile {
    bashBuild("native-lib") {
        tasks.compileJava.dependsOn(this)
        workFolder = File(buildDir, "cmake/native-lib")
            .also { it.mkdirs() }
            .path
        val libsDir = File(buildDir, "libs")
        val cmakeFile = file("src/main/cpp/CMakeLists.txt")
        sh("cmake install", "-B", workFolder, "-S", cmakeFile.parent)
        sh("make all")

        doLast {
            copy {
                from(File(workFolder, "libnative_lib.so"))
                into(libsDir)
            }
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

