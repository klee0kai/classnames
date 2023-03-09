package com.github.klee0kai.test

import com.github.klee0kai.example.Main
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ExampleUnitTest {

    @Test
    fun sayHello() {
        Main.sayHello()
    }

    @Test
    fun someClassIsObfuscated() {
        assertNotEquals("com.github.klee0kai.example.obfuscating.SomeClass", Main.targetClassProxy.canonicalName)
    }

}