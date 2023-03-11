package com.github.klee0kai.classnames.processor.model.clnameannotaion

import com.github.klee0kai.classnames.ClassNames

fun ClassNames.asClNamesAnn(): ClNamesAnn =
    ClNamesAnn(
        indexParent = indexParents
    )

