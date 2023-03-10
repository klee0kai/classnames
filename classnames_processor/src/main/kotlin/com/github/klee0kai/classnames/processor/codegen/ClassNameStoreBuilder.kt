package com.github.klee0kai.classnames.processor.codegen

import com.github.klee0kai.classnames.processor.model.ClassDetail
import com.github.klee0kai.classnames.processor.poet.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.util.*


fun genClassNameStore(
    classname: ClassName,
    indexedClasses: List<ClassDetail>
) {
    val clType = Class::class.asClassName().parameterizedBy(STAR)
    val strType = String::class.asTypeName()
    val clEntryType = ClassName(classname.packageName, "ClEntry")
    val mask = UUID.randomUUID().toString()

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
                    indexedClasses.map {
                        CodeBlock.of(
                            "%T(%L,%T::class.java)\n",
                            clEntryType,
                            it.className.canonicalName.xorToBytes(mask).initCodeBlock(),
                            it.className
                        )
                    }.toCodeBlock()
                )
            }

            genProperty(
                name = "names",
                type = Map::class.asClassName().parameterizedBy(ByteArray::class.asTypeName(), Int::class.asTypeName())
            ) {
                initializer(
                    "mapOf(\n %L )",
                    indexedClasses.mapIndexed { index, it ->
                        CodeBlock.of("%L to $index \n", it.className.canonicalName.xorToBytes(mask).initCodeBlock())
                    }.toCodeBlock()
                )
            }
            genProperty(
                name = "classes",
                type = Map::class.asClassName().parameterizedBy(clType, Int::class.asTypeName())
            ) {
                initializer(
                    "mapOf(\n %L )",
                    indexedClasses.mapIndexed { index, it ->
                        CodeBlock.of("%T::class.java to $index \n", it.className)
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

            addStatement("val index = %T.names.getOrDefault(this.xorToBytes(%T.mask), null)", classname, classname)
            addStatement("return index?.let{ %T.clEntries[it] }", classname);
        }

        genFun("findCompileClassname") {
            addKdoc("Get canonicalName of class. Saved in compile time")
            receiver(clType)
            returns(clEntryType.copy(true))

            addStatement("val index = %T.classes.getOrDefault(this, null)", classname)
            addStatement("return index?.let{ %T.clEntries[it] }", classname);
        }

        genFun("xorToString") {
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

        genFun("xorToBytes") {
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
    }

}


private fun String.xorToBytes(that: String) =
    mapIndexed { index, c ->
        that[index.mod(that.length)].code.xor(c.code)
    }.joinToString(separator = "") {
        it.toChar().toString()
    }.toByteArray()


private fun ByteArray.xorToString(that: String) =
    mapIndexed { index, c ->
        that[index.mod(that.length)].code.xor(c.toInt())
    }.joinToString(separator = "") {
        it.toChar().toString()
    }


private fun ByteArray.initCodeBlock() =
    CodeBlock.of("byteArrayOf (%L)", joinToString { it.toString() })
