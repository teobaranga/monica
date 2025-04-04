package com.teobaranga.monica.core.ui.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

/**
 * Returns a [DateTimeFormatter] that formats dates according to the user's locale.
 */
@Composable
fun rememberLocalizedDateFormatter(
    dateStyle: FormatStyle = FormatStyle.LONG,
    includeYear: Boolean = true,
): DateTimeFormatter {
    val locale = LocalConfiguration.current.locales[0]
    return remember(locale, dateStyle, includeYear) {
        var format = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            /* dateStyle = */ dateStyle,
            /* timeStyle = */ null,
            /* chrono = */ Chronology.ofLocale(locale),
            /* locale = */ locale,
        )
        if (!includeYear) {
            format = format
                .replace("(,*|\'de\') [yY]+$".toRegex(), "")
                .trim()
        }
        DateTimeFormatter.ofPattern(format)
    }
}
