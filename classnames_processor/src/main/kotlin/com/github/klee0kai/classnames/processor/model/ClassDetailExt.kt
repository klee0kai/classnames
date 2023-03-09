package com.github.klee0kai.classnames.processor.model

import java.util.*
import javax.lang.model.element.Modifier

val ClassDetail.isAbstractClass: Boolean
    get() = modifiers.contains(Modifier.ABSTRACT)

fun ClassDetail.getAllParents(includeObject: Boolean): List<ClassDetail> {
//    if (!includeObject && className == OBJECT) return emptyList()
    val parents: MutableList<ClassDetail> = LinkedList<ClassDetail>()
    parents.add(this)
    if (superClass != null) parents.addAll(superClass.getAllParents(includeObject))
    interfaces.forEach {
        parents.addAll(it.getAllParents(includeObject))
    }
    return parents
}


