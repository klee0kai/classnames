package com.github.klee0kai.classnames

import java.lang.annotation.*


/**
 * Collect class compile meta
 *  - classnames
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(allowedTargets = [AnnotationTarget.CLASS, AnnotationTarget.TYPE])
annotation class ClassNames(

    /**
     * Collect also parents compile meta
     */
    val indexParents: Boolean
)
