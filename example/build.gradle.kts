import proguard.gradle.ProGuardTask

plugins {
    kotlin("jvm")
    id("com.dorongold.task-tree") version "2.1.1"
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}


val obfuscateTask = tasks.register<ProGuardTask>("obfuscate") {
    dontwarn()
    dependsOn(tasks.findByName("jar"))
    description = "Obfuscate generated jar file"

    configuration("proguard-rules.pro")
    injars("build/libs/example.jar")
    outjars("build/libs/example_obfuscated.jar")

}

tasks.getByName("assemble") {
    dependsOn.clear()
    dependsOn(obfuscateTask)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

afterEvaluate {
    tasks
}