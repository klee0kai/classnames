package com.github.klee0kai.example

object Main {

    val someClassProxy = SomeClass::class.java

    fun sayHello() {
        SomeClass().sayHello()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        sayHello()

    }

}