plugins {
    alias(libs.plugins.jvm)
//    alias(libs.plugins.publish.maven)
    id("maven-publish")
}

group = rootProject.extra["group"].toString()
version = rootProject.extra["version"].toString()


publishing {
    publications.register<MavenPublication>(name) {
        groupId = group.toString()
        artifactId = project.name
        version = project.version.toString()
        from(components.getByName("java"))

        pom {
            name.set(project.name)
            description.set("Brooklyn bridge annotations")
        }
    }
}