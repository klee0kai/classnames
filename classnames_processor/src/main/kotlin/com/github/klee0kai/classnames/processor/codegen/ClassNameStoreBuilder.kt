package com.github.klee0kai.classnames.processor.codegen

import com.github.klee0kai.classnames.processor.model.ClassDetail
import com.github.klee0kai.classnames.processor.poet.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy


fun genClassNameStore(
    classname: ClassName,
    indexedClasses: List<ClassDetail>
) {
    val clType = Class::class.asClassName().parameterizedBy(STAR)
    val strType = String::class.asTypeName()
    val clEntryType = ClassName(classname.packageName, "ClEntry")

    genKtFile(classname.packageName, classname.simpleName) {
        genLibComment()

        genObject(classname) {
            addModifiers(KModifier.PRIVATE)
            genProperty("clEntries", List::class.asClassName().parameterizedBy(clEntryType)) {
                initializer(
                    "listOf( %L )",
                    indexedClasses.map {
                        CodeBlock.of(
                            "%T(%S,%T::class.java)",
                            clEntryType,
                            it.className.canonicalName,
                            it.className
                        )
                    }.toCodeBlock()
                )
            }

            genProperty("names", Map::class.asClassName().parameterizedBy(strType, Int::class.asTypeName())) {
                initializer(
                    "mapOf( %L )",
                    indexedClasses.mapIndexed { index, it ->
                        CodeBlock.of("%S to $index", it.className.canonicalName)
                    }.toCodeBlock()
                )
            }
            genProperty("classes", Map::class.asClassName().parameterizedBy(clType, Int::class.asTypeName())) {
                initializer(
                    "mapOf( %L )",
                    indexedClasses.mapIndexed { index, it ->
                        CodeBlock.of("%T::class.java to $index", it.className)
                    }.toCodeBlock()
                )
            }
        }

        genClass(clEntryType) {
            addModifiers(KModifier.DATA)
            val pkgProperty = genProperty("canonicalName", String::class.asClassName()) { initFromConstructor() }
            val clProperty = genProperty("cl", clType) { initFromConstructor() }

            genPrimaryConstructor {
                addParameter(pkgProperty.asParameter())
                addParameter(clProperty.asParameter())
            }

        }



        genFun("classnameOfName") {
            addKdoc("Get Class of canonicalName. Saved in compile time")
            receiver(strType)
            returns(clEntryType.copy(true))

            addStatement("val index = %T.names.getOrDefault(this, null)", classname)
            addStatement("return index?.let{ %T.clEntries[it] }", classname);
        }

        genFun("classnameOfClass") {
            addKdoc("Get canonicalName of class. Saved in compile time")
            receiver(clType)
            returns(clEntryType.copy(true))

            addStatement("val index = %T.classes.getOrDefault(this, null)", classname)
            addStatement("return index?.let{ %T.clEntries[it] }", classname);
        }

    }
}