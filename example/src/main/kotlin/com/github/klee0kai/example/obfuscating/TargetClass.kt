package com.github.klee0kai.example.obfuscating

import com.github.klee0kai.classnames.ClassNames
import java.net.URISyntaxException


@ClassNames(indexParents = true)
class TargetClass : ParentClass() {

    fun sayHello() {
        println("Hello from ${this.javaClass}")

        try {
            // Get path of the JAR file
            val jarPath: String = this::class.java
                .protectionDomain
                .codeSource
                .location
                .toURI()
                .path

            println("JAR Path: $jarPath")

            // Get name of the JAR file
            val jarName = jarPath.substring(jarPath.lastIndexOf("/") + 1)
            println("JAR Name: $jarName")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

}