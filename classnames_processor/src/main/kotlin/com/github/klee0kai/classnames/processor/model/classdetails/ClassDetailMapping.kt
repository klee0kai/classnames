package com.github.klee0kai.classnames.processor.model.classdetails

import com.github.klee0kai.classnames.ClassNames
import com.github.klee0kai.classnames.processor.model.clnameannotaion.asClNamesAnn
import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType


fun TypeElement.toClassDetail(): ClassDetail {

    return ClassDetail(
        className = ClassName.bestGuess(qualifiedName.toString()),
        modifiers = modifiers,
        superClass = (superclass as? DeclaredType)?.toClassDetail(),
        interfaces = interfaces.mapNotNull { (it as? DeclaredType)?.toClassDetail() },
        clNamesAnn = getAnnotation(ClassNames::class.java)?.asClNamesAnn()
    )
}

fun DeclaredType.toClassDetail() =
    (asElement() as? TypeElement)?.toClassDetail()



