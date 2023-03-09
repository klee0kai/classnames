package com.github.klee0kai.classnames.processor.codegen

import com.github.klee0kai.classnames.processor.AnnotationProcessor
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.IOException

fun TypeSpec.writeToKt(pkgName: String) {
    try {
        FileSpec.builder(pkgName, this.name.toString())
            .addFileComment("Generated by ClassNames Library\n")
            .addFileComment("Project " + AnnotationProcessor.PROJECT_URL + "\n")
            .addFileComment("Copyright (c) 2023 Andrey Kuzubov")
            .addType(this)
            .build()
            .writeTo(AnnotationProcessor.env.filer)
    } catch (ignore: IOException) {
        // ignore doubles
    }
}