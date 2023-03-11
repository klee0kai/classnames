package com.github.klee0kai.classnames.processor.model.classdetails

import com.squareup.kotlinpoet.ClassName
import java.util.*
import javax.lang.model.element.Modifier

private val objectClName: ClassName = ClassName.bestGuess("java.lang.Object")

val ClassDetail.isAbstractClass: Boolean
    get() = modifiers.contains(Modifier.ABSTRACT)

fun ClassDetail.getAllParents(includeObject: Boolean): List<ClassDetail> {
    if (!includeObject && className == objectClName) return emptyList()
    val parents: MutableList<ClassDetail> = LinkedList<ClassDetail>()
    parents.add(this)
    if (superClass != null) parents.addAll(superClass.getAllParents(includeObject))
    interfaces.forEach {
        parents.addAll(it.getAllParents(includeObject))
    }
    return parents
}


