package com.github.klee0kai.classnames.processor.model

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType


fun TypeElement.toClassDetail(): ClassDetail =
    ClassDetail(
        className = ClassName.bestGuess(qualifiedName.toString()),
        modifiers = modifiers,
        superClass = (superclass as? DeclaredType)?.toClassDetail(),
        interfaces = interfaces.mapNotNull { (it as? DeclaredType)?.toClassDetail() },
    )

fun DeclaredType.toClassDetail() =
    (asElement() as? TypeElement)?.toClassDetail()


