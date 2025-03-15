package com.teobaranga.monica

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionContainer
import javax.inject.Inject

/**
 * Configure various build properties.
 *
 * Currently only supports configuring some opt-in flags.
 */
open class MonicaExtension @Inject constructor(
    objects: ObjectFactory,
) {
    val optIn = objects.newInstance(OptInHandler::class.java)

    fun optIn(action: Action<OptInHandler>) {
        action.execute(optIn)
    }

    companion object {
        fun ExtensionContainer.monica(): MonicaExtension {
            return create("monica", MonicaExtension::class.java)
        }
    }
}

open class OptInHandler @Inject constructor() {

    var experimentalCoroutinesApi: Boolean = true

    var flowPreview: Boolean = true
}
