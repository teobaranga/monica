package com.teobaranga.monica.core.ui.datetime

import androidx.compose.ui.text.intl.PlatformLocale
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.core.datetime.toJavaMonthDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaMonth
import kotlinx.datetime.toJavaYearMonth
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.format.TextStyle

actual class LocalDateFormatter actual constructor(
    private val locale: PlatformLocale,
    private val dateFormatStyle: DateFormatStyle,
    includeYear: Boolean,
) {
    private val formatter: DateTimeFormatter

    init {
        val dateStyle = when (dateFormatStyle) {
            DateFormatStyle.SHORT -> FormatStyle.SHORT
            DateFormatStyle.MEDIUM -> FormatStyle.MEDIUM
            DateFormatStyle.LONG -> FormatStyle.LONG
            DateFormatStyle.FULL -> FormatStyle.FULL
        }
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
        formatter = DateTimeFormatter.ofPattern(format)
    }

    actual fun format(date: LocalDate): String {
        return date.toJavaLocalDate().format(formatter)
    }

    actual fun format(monthDay: MonthDay) : String {
        return monthDay.toJavaMonthDay().format(formatter)
    }

    actual fun format(yearMonth: YearMonth): String {
        return yearMonth.toJavaYearMonth().format(formatter)
    }

    actual fun format(month: Month): String {
        val style = when (dateFormatStyle) {
            DateFormatStyle.SHORT -> TextStyle.NARROW
            DateFormatStyle.MEDIUM -> TextStyle.SHORT
            DateFormatStyle.LONG -> TextStyle.FULL
            DateFormatStyle.FULL -> TextStyle.FULL
        }
        return month.toJavaMonth().getDisplayName(style, locale)
    }
}
