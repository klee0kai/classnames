package com.github.klee0kai.test

import com.github.klee0kai.classnames.findCompileClassname
import com.github.klee0kai.example.Main
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExampleUnitTest {

    val targetClName = "com.github.klee0kai.example.obfuscating.TargetClass"

    @Test
    fun sayHello() {
        Main.sayHello()
    }

    @Test
    fun someClassIsObfuscated() {
        assertNotEquals(targetClName, Main.targetClassProxy.canonicalName)
    }


    @Test
    fun compileTimeClassName() {

        //when
        val clEntry = targetClName.findCompileClassname()

        //Then
        assertEquals(targetClName, clEntry?.canonicalName)
        assertEquals(Main.targetClassProxy, clEntry?.cl)
    }

    @Test
    fun compileTimeClass() {

        //when
        val clEntry = Main.targetClassProxy.findCompileClassname()

        //Then
        assertEquals(targetClName, clEntry?.canonicalName)
        assertEquals(Main.targetClassProxy, clEntry?.cl)
    }

    @Test
    fun crashChecks() {
        assertNull("olla".findCompileClassname())
        assertNull(String::class.java.findCompileClassname())
    }
}