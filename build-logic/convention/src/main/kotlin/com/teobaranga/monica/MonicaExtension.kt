package com.teobaranga.monica

import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import javax.inject.Inject

/**
 * Configure various build properties.
 */
open class MonicaExtension @Inject constructor(
    objects: ObjectFactory,
) {

    companion object {
        fun ExtensionContainer.monica(): MonicaExtension {
            return findByType<MonicaExtension>()
                ?: create<MonicaExtension>("monica")
        }
    }
}
