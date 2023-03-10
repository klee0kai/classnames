package com.github.klee0kai.classnames.processor

import com.github.klee0kai.classnames.ClassNames
import com.github.klee0kai.classnames.processor.codegen.genClassNameStore
import com.github.klee0kai.classnames.processor.model.toClassDetail
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedAnnotationTypes("*")
class AnnotationProcessor : AbstractProcessor() {
    companion object {
        val PROJECT_URL = "https://github.com/klee0kai/ClassNames"
        lateinit var env: ProcessingEnvironment
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        env = processingEnv
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val classes = roundEnv.getElementsAnnotatedWith(ClassNames::class.java).mapNotNull { element ->
            (element as? TypeElement)?.toClassDetail()
        }

        genClassNameStore(
            classname = ClassName("com.github.klee0kai.classnames", "ClNamesStore"),
            indexedClasses = classes
        )

        return true
    }


}