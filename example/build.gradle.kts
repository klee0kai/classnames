import proguard.gradle.ProGuardTask

plugins {
    kotlin("jvm")
    kotlin("kapt")
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
    injars(files("build/libs/example-packed.jar"))
    outjars(files("build/libs/example-obfuscated.jar"))
}

tasks.register<Test>("test_non_obfuscated") {
    description = "Run tests on non obfuscated jar file"
    dependsOn(packingTest)
    useJUnitPlatform()

    val mainFiles = sourceSets.main.get().output.files
    classpath = classpath.filter { !mainFiles.contains(it) }
    classpath += files("build/libs/example-packed.jar")
}


tasks.getByName("assemble") {
    dependsOn(obfuscateTask)
}

tasks.getByName<Test>("test") {
    dependsOn(obfuscateTask)
    useJUnitPlatform()

    val mainFiles = sourceSets.main.get().output.files
    classpath = classpath.filter { !mainFiles.contains(it) }
    classpath += files("build/libs/example-obfuscated.jar")
}



dependencies {
    kapt(project(":classnames_processor"))
    implementation(project(":classnames"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
