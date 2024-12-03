package com.teobaranga.monica.inject.runtime

import software.amazon.lastmile.kotlin.inject.anvil.extend.ContributingAnnotation
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@ContributingAnnotation
annotation class ContributesViewModel(
    /**
     * The scope in which to include this ViewModel.
     */
    val scope: KClass<*>,
)
