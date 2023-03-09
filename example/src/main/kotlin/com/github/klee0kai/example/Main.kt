package com.github.klee0kai.example

import com.github.klee0kai.example.obfuscating.TargetClass

object Main {

    val targetClassProxy = TargetClass::class.java

    fun sayHello() {
        TargetClass().sayHello()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        sayHello()

    }

}