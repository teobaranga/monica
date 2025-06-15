package com.teobaranga.monica

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.property
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

    val inject = objects.newInstance(InjectHandler::class.java)

    fun optIn(action: Action<OptInHandler>) {
        action.execute(optIn)
    }

    fun inject(action: Action<InjectHandler>) {
        action.execute(inject)
    }

    companion object {
        fun ExtensionContainer.monica(): MonicaExtension {
            return findByType<MonicaExtension>()
                ?: create<MonicaExtension>("monica")
        }
    }
}

open class OptInHandler @Inject constructor() {

    var experimentalCoroutinesApi: Boolean = true

    var flowPreview: Boolean = true
}

open class InjectHandler @Inject constructor(project: Project) {

    val injectIn = project.objects.property<Target>()
        .convention(Target.COMMON)

    enum class Target {

        /**
         * Generate code only in commonMain.
         */
        COMMON,

        /**
         * Generate code in all target source sets separately.
         */
        SEPARATE,
    }
}
