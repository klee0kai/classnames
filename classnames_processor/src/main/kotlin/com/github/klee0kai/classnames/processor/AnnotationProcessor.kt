package com.github.klee0kai.classnames.processor

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedAnnotationTypes("*")
class AnnotationProcessor : AbstractProcessor() {

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {

        return true
    }


}