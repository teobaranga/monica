package com.teobaranga.monica.contacts.edit.birthday

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runAndroidComposeUiTest
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.datetime.FixedClock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.Month
import java.time.ZoneId
import java.util.Locale
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class BirthdaySectionTest {

    @Test
    fun `Given full birthday, date displays correctly`() = runAndroidComposeUiTest<ComponentActivity> {
        setupLocale()

        val date = LocalDate(1995, 1, 1)
        val now = Instant.parse("2025-04-26T12:00:00Z")
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")))

        setContent {
            CompositionLocalProvider(
                LocalSystemClock provides FixedClock(now),
            ) {
                BirthdaySection(
                    birthday = Birthday.Full(date),
                    onBirthdayChange = { },
                )
            }
        }

        onNodeWithText("January 1, 1995 (30 years old)")
            .assertIsDisplayed()
    }

    @Test
    fun `Given age based birthday, date displays correctly`() = runAndroidComposeUiTest<ComponentActivity> {
        setupLocale()

        val age = 30

        setContent {
            BirthdaySection(
                birthday = Birthday.AgeBased(age),
                onBirthdayChange = { },
            )
        }

        onNodeWithText("~30 years old")
            .assertIsDisplayed()
    }

    @Test
    fun `Given unknown year birthday, date displays correctly`() = runAndroidComposeUiTest<ComponentActivity> {
        setupLocale()

        val monthDay = MonthDay.of(Month.JANUARY, 1)

        setContent {
            BirthdaySection(
                birthday = Birthday.UnknownYear(monthDay),
                onBirthdayChange = { },
            )
        }

        onNodeWithText("January 1")
            .assertIsDisplayed()
    }

    private fun AndroidComposeUiTest<*>.setupLocale() {
        RuntimeEnvironment.setQualifiers("en-rUS")
        Locale.setDefault(activity!!.resources.configuration.locales[0])
    }
}
