package com.github.klee0kai.classnames.processor.codegen

import com.github.klee0kai.classnames.processor.ext.xorToBytes
import com.github.klee0kai.classnames.processor.ext.xorToBytesFun
import com.github.klee0kai.classnames.processor.ext.xorToStringFun
import com.github.klee0kai.classnames.processor.model.classdetails.ClassDetail
import com.github.klee0kai.classnames.processor.model.classdetails.getAllParents
import com.github.klee0kai.classnames.processor.poet.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.util.*


fun genClassNameStore(
    classname: ClassName,
    collectClasses: List<ClassDetail>
) {
    val clType = Class::class.asClassName().parameterizedBy(STAR)
    val strType = String::class.asTypeName()
    val clEntryType = ClassName(classname.packageName, "ClEntry")
    val mask = UUID.randomUUID().toString()
    val allIndexClasses = collectClasses.flatMap {
        if (it.clNamesAnn?.indexParent == true) {
            it.getAllParents(false)
        } else {
            listOf(it)
        }
    }

    genKtFile(classname.packageName, classname.simpleName) {
        genLibComment()

        genObject(classname) {
            addModifiers(KModifier.PRIVATE)
            genProperty("mask", String::class.asTypeName()) {
                initializer("%S", mask)
            }
            genProperty(
                name = "clEntries",
                type = List::class.asClassName().parameterizedBy(clEntryType)
            ) {
                initializer(
                    "listOf(\n %L )",
                    allIndexClasses.map {
                        CodeBlock.of(
                            "%T(%L,%T::class.java),\n",
                            clEntryType,
                            it.className.canonicalName.xorToBytes(mask).initCodeBlock(),
                            it.className
                        )
                    }.toCodeBlock()
                )
            }

            genProperty(
                name = "names",
                type = Map::class.asClassName().parameterizedBy(Int::class.asTypeName(), Int::class.asTypeName())
            ) {
                initializer(
                    "mapOf(\n %L )",
                    allIndexClasses.mapIndexed { index, it ->
                        CodeBlock.of("${it.className.canonicalName.hashCode()} to $index,\n")
                    }.toCodeBlock()
                )
            }
            genProperty(
                name = "classes",
                type = Map::class.asClassName().parameterizedBy(Int::class.asTypeName(), Int::class.asTypeName())
            ) {
                initializer(
                    "mapOf(\n %L )",
                    allIndexClasses.mapIndexed { index, it ->
                        CodeBlock.of("%T::class.java.hashCode() to $index,\n", it.className)
                    }.toCodeBlock()
                )
            }
        }

        genClass(clEntryType) {
            addModifiers(KModifier.DATA)
            val canonicalNameProperty =
                genProperty("canonicalNameMasked", ByteArray::class.asClassName()) {
                    addModifiers(KModifier.PRIVATE)
                    initFromConstructor()
                }
            val clProperty = genProperty("cl", clType) { initFromConstructor() }

            genPrimaryConstructor {
                addParameter(canonicalNameProperty.asParameter())
                addParameter(clProperty.asParameter())
            }

            genProperty("canonicalName", String::class.asClassName()) {
                genGetter {
                    addCode("return canonicalNameMasked.xorToString(%T.mask)", classname)
                }
            }

        }


        genFun("findCompileClassname") {
            addKdoc("Get Class of canonicalName. Saved in compile time")
            receiver(strType)
            returns(clEntryType.copy(true))

            addStatement("val index = %T.names.getOrDefault(hashCode(), null)", classname)
            addStatement("return index?.let{ %T.clEntries[it] }", classname)
        }

        genFun("findCompileClassname") {
            addKdoc("Get canonicalName of class. Saved in compile time")
            receiver(clType)
            returns(clEntryType.copy(true))

            addStatement("val index = %T.classes.getOrDefault(hashCode() , null)", classname)
            addStatement("return index?.let{ %T.clEntries[it] }", classname)
        }

        genFun("xorToString") {
            xorToStringFun()
        }

        genFun("xorToBytes") {
            xorToBytesFun()
        }
    }

}


private fun ByteArray.initCodeBlock() =
    CodeBlock.of("byteArrayOf (%L)", joinToString { it.toString() })
