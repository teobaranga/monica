package com.teobaranga.monica.inject.runtime

import me.tatarka.inject.annotations.Qualifier

/** Annotation for an Application Context dependency. */
@Qualifier
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
annotation class ApplicationContext
