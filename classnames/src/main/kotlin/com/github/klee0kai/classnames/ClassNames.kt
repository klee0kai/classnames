package com.github.klee0kai.classnames

import java.lang.annotation.*


@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(allowedTargets = [AnnotationTarget.CLASS, AnnotationTarget.TYPE])
annotation class ClassNames(
    /**
     * Collect also original method names
     */
    val indexMethods: Boolean
)
