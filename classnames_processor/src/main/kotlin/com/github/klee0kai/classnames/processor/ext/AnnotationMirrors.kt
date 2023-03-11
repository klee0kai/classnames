package com.github.klee0kai.classnames.processor.ext

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.AnnotationMirror

fun Collection<AnnotationMirror>.findAnnotationMirror(className: ClassName) =
    firstOrNull { it.annotationType.toString() == className.canonicalName }


fun AnnotationMirror.keyBySimpleName(name: String) =
    elementValues.keys.firstOrNull { it.simpleName.toString() == name }

fun AnnotationMirror.valueOfSimpleName(name: String) =
    elementValues.getOrDefault(keyBySimpleName(name), null)?.value

