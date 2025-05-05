package com.teobaranga.monica.core.ui.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.PlatformLocale
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.LocalDate

enum class DateFormatStyle {
    SHORT,
    MEDIUM,
    LONG,
    FULL,
}

expect class LocalDateFormatter(
    locale: PlatformLocale,
    dateFormatStyle: DateFormatStyle = DateFormatStyle.LONG,
    includeYear: Boolean = true,
) {
    fun format(date: LocalDate): String
    fun format(monthDay: MonthDay): String
}

/**
 * Returns a [LocalDateFormatter] that formats dates according to the user's locale.
 */
@Composable
fun rememberLocalizedDateFormatter(
    dateStyle: DateFormatStyle = DateFormatStyle.LONG,
    includeYear: Boolean = true,
): LocalDateFormatter {
    val locale = Locale.current.platformLocale
    return remember(locale, dateStyle, includeYear) {
        LocalDateFormatter(locale, dateStyle, includeYear)
    }
}
