package com.teobaranga.monica

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.SpecExecutionOrder
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter

class KotestConfig : AbstractProjectConfig() {

    override val specExecutionOrder = SpecExecutionOrder.Annotated

    override val extensions: List<Extension>
        get() = listOf(
            JunitXmlReporter(
                includeContainers = false,
                useTestPathAsName = true,
            ),
            HtmlReporter(),
        )
}
