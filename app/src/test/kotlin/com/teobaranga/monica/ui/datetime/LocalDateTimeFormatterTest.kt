package com.teobaranga.monica.ui.datetime

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runAndroidComposeUiTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.LocalDateTime
import java.util.Locale

@RunWith(ParameterizedRobolectricTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class LocalDateTimeFormatterTest(
    private val locale: String,
    private val withYear: Boolean,
    private val expected: String,
) {
    private val date = LocalDateTime.of(
        /* year = */ 2025,
        /* month = */ 2,
        /* dayOfMonth = */ 24,
        /* hour = */ 0,
        /* minute = */ 0,
    )

    @Test
    fun `Format is correct - `() = runAndroidComposeUiTest<ComponentActivity> {
        RuntimeEnvironment.setQualifiers(locale)
        Locale.setDefault(activity!!.resources.configuration.locales[0])
        setContent {
            val formatter = rememberLocalizedDateFormatter(includeYear = withYear)
            val formatted = formatter.format(date)
            assertEquals(expected, formatted)
        }
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "locale: {0}, with year: {1} -> {2}")
        fun params() = listOf<Array<Any>>(
            arrayOf("en-rUS", false, "February 24"),
            arrayOf("en-rUS", true, "February 24, 2025"),

            arrayOf("en-rGB", false, "24 February"),
            arrayOf("en-rGB", true, "24 February 2025"),

            arrayOf("fr-rFR", false, "24 février"),
            arrayOf("fr-rFR", true, "24 février 2025"),

            arrayOf("it-rIT", false, "24 febbraio"),
            arrayOf("it-rIT", true, "24 febbraio 2025"),

            arrayOf("es-rES", false, "24 de febrero"),
            arrayOf("es-rES", true, "24 de febrero de 2025"),

            arrayOf("de-rDE", false, "24. Februar"),
            arrayOf("de-rDE", true, "24. Februar 2025"),
        )
    }
}
