package com.teobaranga.monica

import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.PrintLogger
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.test.TestCaseOrder
import io.kotest.engine.concurrency.TestExecutionMode
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class KotestConfig : AbstractProjectConfig() {

    override val testCaseOrder: TestCaseOrder = TestCaseOrder.Sequential

    override val isolationMode: IsolationMode = IsolationMode.InstancePerRoot

    override val testExecutionMode: TestExecutionMode = TestExecutionMode.Sequential

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

        Dispatchers.setMain(UnconfinedTestDispatcher())

        KmLogging.clear()
        KmLogging.addLogger(PrintLogger(FixedLogLevel(true)))
    }

    override suspend fun afterProject() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")

        Dispatchers.resetMain()
    }
}
