import com.android.build.gradle.internal.tasks.factory.dependsOn
import proguard.gradle.ProGuardTask

plugins {
    kotlin("jvm")
    id("brooklyn")
    id("klee0kai-crosscompile")
    id("com.github.gmazzo.buildconfig") version ("3.1.0")
}

group = "com.klee0kai.example"

sourceSets {
    main {
        brooklyn {
            enabled = true
            cacheFile = null
        }
    }
}

buildConfig {
    buildConfigField("String", "NATIVE_LIB_PATH", "\"${File(buildDir, "libs/libnative_lib.so").absolutePath}\"")
}

val packingTest = tasks.register<Jar>("jarMain") {
    description = "Build jar artifact"
    dependsOn(tasks.findByName("classes"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Implementation-Title"] = "Obfuscated simple jar application"
        attributes["Main-Class"] = "com.klee0kai.example.Main"
    }
    baseName = project.name + "-packed"
    println("conf: ${configurations.compileClasspath.get().map { it.path }}")
    from(
        configurations.compileClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
    from(sourceSets.main.get().output)
}

val obfuscateTask = tasks.register<ProGuardTask>("obfuscate") {
    description = "Obfuscate generated jar file"
    dontwarn()
    dependsOn(packingTest)

    configuration("proguard-rules.pro")
    injars(files("build/libs/example-packed.jar"))
    outjars(files("build/libs/example-obfuscated.jar"))
    printmapping("build/libs/example-obfuscated.map")
}

tasks.getByName("assemble") {
    dependsOn(obfuscateTask)
}


crosscompile {
    bashBuild("native-lib") {
        this.dependsOn(tasks.compileJava)
        tasks.jar.dependsOn(this)
        workFolder = File(buildDir, "cmake/native-lib")
            .also { it.mkdirs() }
            .path
        val libsDir = File(buildDir, "libs")
        val cmakeFile = file("src/main/cpp/CMakeLists.txt")
        sh("cmake install", "-B", workFolder, "-S", cmakeFile.parent, "-DCMAKE_BUILD_TYPE=Debug")
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
    dependsOn(obfuscateTask)
    useJUnitPlatform()

    val mainFiles = sourceSets.main.get().output.files
    classpath = classpath.filter { !mainFiles.contains(it) }
    classpath += files("build/libs/example-obfuscated.jar")
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

