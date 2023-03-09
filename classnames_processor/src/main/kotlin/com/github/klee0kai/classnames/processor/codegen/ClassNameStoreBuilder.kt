package com.github.klee0kai.classnames.processor.codegen

import com.github.klee0kai.classnames.processor.model.ClassDetail
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec

class ClassNameStoreBuilder(
    private val classname: ClassName
) {


    private val collectRuns = mutableListOf<() -> Unit>()

    fun indexNewClass(classname: ClassDetail) {

    }


    fun build() {
        collectRuns.forEach { it.invoke() }
        collectRuns.clear()

        val typeSpec = TypeSpec.classBuilder(classname)
            .build()
        typeSpec.writeToKt(classname.packageName)
    }

}