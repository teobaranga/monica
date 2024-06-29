package com.teobaranga.monica.ui.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle

val LocalDateFormatter = staticCompositionLocalOf<DateTimeFormatter> {
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
}

val LocalMonthDayFormatter = staticCompositionLocalOf<DateTimeFormatter> {
    error("CompositionLocal LocalMonthDayFormatter not present")
}

@Composable
fun getMonthDayFormatter(dateStyle: FormatStyle = FormatStyle.MEDIUM): DateTimeFormatter {
    val locale = LocalConfiguration.current.locales[0]
    val format = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        /* dateStyle = */ dateStyle,
        /* timeStyle = */ null,
        /* chrono = */ Chronology.ofLocale(locale),
        /* locale = */ locale,
    ).filterNot {
        // Remove year
        val lowercaseChar = it.lowercaseChar()
        lowercaseChar == 'y' || lowercaseChar == ','
    }.trim()
    return DateTimeFormatter.ofPattern(format)
}
