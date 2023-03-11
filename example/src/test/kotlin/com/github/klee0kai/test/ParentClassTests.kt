package com.github.klee0kai.test

import com.github.klee0kai.classnames.findCompileClassname
import com.github.klee0kai.example.Main
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ParentClassTests {

    private val parentClName = "com.github.klee0kai.example.obfuscating.ParentClass"

    @Test
    fun classIsObfuscated() {
        assertNotEquals(parentClName, Main.parentClassProxy.canonicalName)
    }


    @Test
    fun compileTimeClassName() {

        //when
        val clEntry = parentClName.findCompileClassname()

        //Then
        assertEquals(parentClName, clEntry?.canonicalName)
        assertEquals(Main.parentClassProxy, clEntry?.cl)
    }

    @Test
    fun compileTimeClass() {

        //when
        val clEntry = Main.parentClassProxy.findCompileClassname()

        //Then
        assertEquals(parentClName, clEntry?.canonicalName)
        assertEquals(Main.parentClassProxy, clEntry?.cl)
    }


}

