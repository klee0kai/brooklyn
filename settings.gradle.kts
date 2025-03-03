pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        classpath("com.github.klee0kai:crosscompile:0.0.1")
    }
}

rootProject.name = "BrooklynBridge"
includeBuild("brooklyn")
include("tests")
