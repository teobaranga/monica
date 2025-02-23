package com.teobaranga.monica.ui.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

val LocalDateFormatter = staticCompositionLocalOf<DateTimeFormatter> {
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
}

@Composable
fun rememberLocalizedDateTimeFormatter(
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
            format = format.replace(", [yY]+$".toRegex(), "")
        }
        DateTimeFormatter.ofPattern(format)
    }
}
