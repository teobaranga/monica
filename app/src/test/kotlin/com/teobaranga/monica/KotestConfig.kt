package com.teobaranga.monica

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.SpecExecutionOrder
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter
import io.mockk.mockkStatic
import io.mockk.unmockkStatic

class KotestConfig : AbstractProjectConfig() {

    override val specExecutionOrder = SpecExecutionOrder.Lexicographic

    override val extensions: List<Extension>
        get() = listOf(
            JunitXmlReporter(
                includeContainers = false,
                useTestPathAsName = true,
            ),
            HtmlReporter(),
        )

    override suspend fun beforeProject() {
        // Use of navigation type-safe APIs forces ViewModel tests to be instrumented
        // https://issuetracker.google.com/issues/349807172?pli=1
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    override suspend fun afterProject() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }
}
