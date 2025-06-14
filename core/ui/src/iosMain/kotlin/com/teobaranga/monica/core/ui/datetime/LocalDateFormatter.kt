package com.teobaranga.monica.core.ui.datetime

import androidx.compose.ui.text.intl.PlatformLocale
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.core.datetime.YearMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toNSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter

// TODO
actual class LocalDateFormatter actual constructor(
    private val locale: PlatformLocale,
    private val dateFormatStyle: DateFormatStyle,
    private val includeYear: Boolean,
) {
    actual fun format(date: LocalDate): String {
        val formatter = NSDateFormatter()
        when (dateFormatStyle) {
            DateFormatStyle.SHORT -> {
                formatter.setLocalizedDateFormatFromTemplate("dd MMMM yyyy")
            }

            DateFormatStyle.MEDIUM -> {
                formatter.setLocalizedDateFormatFromTemplate("dd MMMM yyyy")
            }

            DateFormatStyle.LONG -> {
                formatter.setLocalizedDateFormatFromTemplate("dd MMMM yyyy")
            }

            DateFormatStyle.FULL -> {
                formatter.setLocalizedDateFormatFromTemplate("EEEE, d MMMM, yyyy")
            }
        }
        return formatter.stringFromDate(date.atStartOfDayIn(TimeZone.currentSystemDefault()).toNSDate()).orEmpty()
    }

    actual fun format(monthDay: MonthDay): String {
        return ""
    }

    actual fun format(yearMonth: YearMonth): String {
        return ""
    }

    actual fun format(month: Month): String {
        val formatter = NSDateFormatter()
        when (dateFormatStyle) {
            DateFormatStyle.SHORT -> {
                formatter.setLocalizedDateFormatFromTemplate("M")
            }

            DateFormatStyle.MEDIUM -> {
                formatter.setLocalizedDateFormatFromTemplate("MM")
            }

            DateFormatStyle.LONG -> {
                formatter.setLocalizedDateFormatFromTemplate("MMM")
            }

            DateFormatStyle.FULL -> {
                formatter.setLocalizedDateFormatFromTemplate("MMMM")
            }
        }
        val dateComponents = NSDateComponents()
        dateComponents.month = month.ordinal + 1L
        val date = NSCalendar.currentCalendar().dateFromComponents(dateComponents)!!
        return formatter.stringFromDate(date)
    }
}
