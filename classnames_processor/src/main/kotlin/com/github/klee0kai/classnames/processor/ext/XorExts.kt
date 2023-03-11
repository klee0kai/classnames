package com.github.klee0kai.classnames.processor.ext

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier


fun FunSpec.Builder.xorToBytesFun() {
    addModifiers(KModifier.PRIVATE)
    receiver(String::class)
    addParameter("that", String::class)
    returns(ByteArray::class)

    addCode(
        """
                | return mapIndexed { index, c ->
                |     that[index.mod(that.length)].code.xor(c.code)
                | }.joinToString(separator = "") {
                |     it.toChar().toString()
                | }.toByteArray()
                """.trimMargin()
    )
}

fun FunSpec.Builder.xorToStringFun() {
    addModifiers(KModifier.PRIVATE)
    receiver(ByteArray::class)
    addParameter("that", String::class)
    returns(String::class)

    addCode(
        """
                | return mapIndexed { index, c ->
                |     that[index.mod(that.length)].code.xor(c.toInt())
                | }.joinToString(separator = "") {
                |     it.toChar().toString()
                | }
                """.trimMargin()
    )
}

fun String.xorToBytes(that: String) =
    mapIndexed { index, c ->
        that[index.mod(that.length)].code.xor(c.code)
    }.joinToString(separator = "") {
        it.toChar().toString()
    }.toByteArray()


fun ByteArray.xorToString(that: String) =
    mapIndexed { index, c ->
        that[index.mod(that.length)].code.xor(c.toInt())
    }.joinToString(separator = "") {
        it.toChar().toString()
    }
