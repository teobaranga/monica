package com.teobaranga.monica.ui.datetime

import androidx.compose.runtime.staticCompositionLocalOf
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

val LocalDateFormatter = staticCompositionLocalOf<DateTimeFormatter> {
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
}

val LocalMonthDayFormatter = staticCompositionLocalOf<DateTimeFormatter> {
    error("CompositionLocal LocalMonthDayFormatter not present")
}

fun getDefaultMonthDayFormatter(locale: Locale): DateTimeFormatter {
    val format = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        /* dateStyle = */ FormatStyle.MEDIUM,
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
