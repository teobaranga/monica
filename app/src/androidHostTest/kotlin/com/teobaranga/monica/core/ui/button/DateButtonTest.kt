package com.teobaranga.monica.core.ui.button

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runAndroidComposeUiTest
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.Locale
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class DateButtonTest {

    @Test
    fun `Date is correct`() = runAndroidComposeUiTest<ComponentActivity> {
        setupLocale()

        val date = LocalDate(1995, 1, 1)

        var selectedDate: LocalDate? = null

        setContent {
            DateButton(
                date = date,
                onDateSelect = {
                    selectedDate = it
                },
            )
        }

        onNodeWithText("January 1, 1995")
            .assert(hasClickAction())
            .assertIsDisplayed()
            .performClick()

        onNode(hasText("January 31", substring = true))
            .performClick()

        onNodeWithText("Confirm")
            .performClick()

        selectedDate shouldBe LocalDate(1995, 1, 31)
    }

    private fun AndroidComposeUiTest<*>.setupLocale() {
        RuntimeEnvironment.setQualifiers("en-rUS")
        Locale.setDefault(activity!!.resources.configuration.locales[0])
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"))
    }
}
