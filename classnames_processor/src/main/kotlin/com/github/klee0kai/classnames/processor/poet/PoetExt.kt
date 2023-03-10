package com.github.klee0kai.classnames.processor.poet

import com.github.klee0kai.classnames.processor.AnnotationProcessor
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import java.io.IOException


fun FileSpec.Builder.genLibComment() {
    addFileComment("Generated by ClassNames Library\n")
    addFileComment("Project " + AnnotationProcessor.PROJECT_URL + "\n")
    addFileComment("Copyright (c) 2023 Andrey Kuzubov")
}

fun FileSpec.tryWrite() {
    try {
        writeTo(AnnotationProcessor.env.filer)
    } catch (ignore: IOException) {
        // ignore doubles
    }
}

fun PropertySpec.asParameter(): ParameterSpec = ParameterSpec.builder(name, type).build()

fun PropertySpec.Builder.initFromConstructor(): PropertySpec.Builder = apply { initializer(build().name) }

fun Collection<CodeBlock>.toCodeBlock(): CodeBlock {
    val blocks = this
    return CodeBlock.builder().apply {
        blocks.forEach {
            add(it)
        }
    }.build()
}