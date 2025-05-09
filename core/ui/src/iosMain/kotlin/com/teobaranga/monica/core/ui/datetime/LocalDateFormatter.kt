package com.teobaranga.monica.core.ui.datetime

import androidx.compose.ui.text.intl.PlatformLocale
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.core.datetime.YearMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSDateComponentsFormatter

// TODO
actual class LocalDateFormatter actual constructor(
    private val locale: PlatformLocale,
    private val dateFormatStyle: DateFormatStyle,
    private val includeYear: Boolean,
) {
    actual fun format(date: LocalDate): String {
        val formatter = NSDateComponentsFormatter()
        return formatter.stringFromDateComponents(date.toNSDateComponents()).orEmpty()
    }

    actual fun format(monthDay: MonthDay): String {
        return ""
    }

    actual fun format(yearMonth: YearMonth): String {
        return ""
    }
    actual fun format(month: Month): String {
        return ""
    }
}
