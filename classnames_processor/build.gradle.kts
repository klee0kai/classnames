plugins {
    id("java")
    kotlin("jvm")
    kotlin("kapt")
}

group = "com.github.klee0kai.classnames.processor"
version = "unspecified"



dependencies {
    //base lib
    implementation(project(":classnames"))

    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.google.auto.service:auto-service:1.0.1")

    //incap
    implementation("net.ltgt.gradle.incap:incap:0.3")
    implementation("net.ltgt.gradle.incap:incap-processor:0.3")

    //kotlinpoet
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
}


