import proguard.gradle.ProGuardTask

plugins {
    kotlin("jvm")
    id("com.dorongold.task-tree") version "2.1.1"
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}


val packingTest = tasks.register<Jar>("jarMain") {
    description = "Build jar artifact"
    dependsOn(tasks.findByName("classes"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Implementation-Title"] = "Obfuscated simple jar application"
        attributes["Main-Class"] = "com.github.klee0kai.example.Main"
    }
    baseName = project.name + "-packed"
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
    dependsOn(tasks.findByName("jarMain"))

    configuration("proguard-rules.pro")
    injars("build/libs/example-packed.jar")
    outjars("build/libs/example-obfuscated.jar")
}

tasks.getByName("assemble") {
    dependsOn(obfuscateTask)
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

