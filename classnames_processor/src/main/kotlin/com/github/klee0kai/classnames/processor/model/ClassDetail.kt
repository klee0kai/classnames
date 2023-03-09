package com.github.klee0kai.classnames.processor.model

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.Modifier

data class ClassDetail(
    val className: ClassName,
    val modifiers: Set<Modifier> = emptySet(),
    val superClass: ClassDetail? = null,
    val interfaces: List<ClassDetail> = emptyList(),
)