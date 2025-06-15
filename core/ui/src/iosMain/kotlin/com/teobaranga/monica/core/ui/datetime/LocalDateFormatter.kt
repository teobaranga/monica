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

actual class LocalDateFormatter actual constructor(
    private val locale: PlatformLocale,
    private val dateFormatStyle: DateFormatStyle,
    private val includeYear: Boolean,
) {
    actual fun format(date: LocalDate): String {
        val formatter = NSDateFormatter().apply {
            val format = when (dateFormatStyle) {
                DateFormatStyle.SHORT -> "d MMMM yyyy"
                DateFormatStyle.MEDIUM -> "d MMMM yyyy"
                DateFormatStyle.LONG -> "d MMMM yyyy"
                DateFormatStyle.FULL -> "EEEE, d MMMM, yyyy"
            }
            setLocalizedDateFormatFromTemplate(format)
        }
        val nsDate = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toNSDate()
        return formatter.stringFromDate(nsDate)
    }

    actual fun format(monthDay: MonthDay): String {
        val formatter = NSDateFormatter().apply {
            val format = when (dateFormatStyle) {
                DateFormatStyle.SHORT -> "d M"
                DateFormatStyle.MEDIUM -> "d MM"
                DateFormatStyle.LONG -> "d MMM"
                DateFormatStyle.FULL -> "d MMMM"
            }
            setLocalizedDateFormatFromTemplate(format)
        }
        val dateComponents = NSDateComponents().apply {
            month = monthDay.month.ordinal + 1L
            day = monthDay.dayOfMonth.toLong()
        }
        val date = NSCalendar.currentCalendar().dateFromComponents(dateComponents)
        return formatter.stringFromDate(requireNotNull(date))
    }

    actual fun format(yearMonth: YearMonth): String {
        val formatter = NSDateFormatter().apply {
            val format = when (dateFormatStyle) {
                DateFormatStyle.SHORT -> "M, yyyy"
                DateFormatStyle.MEDIUM -> "MM, yyyy"
                DateFormatStyle.LONG -> "MMM, yyyy"
                DateFormatStyle.FULL -> "MMMM, yyyy"
            }
            setLocalizedDateFormatFromTemplate(format)
        }
        val dateComponents = NSDateComponents().apply {
            month = yearMonth.month.ordinal + 1L
            year = yearMonth.year.toLong()
        }
        val date = NSCalendar.currentCalendar().dateFromComponents(dateComponents)
        return formatter.stringFromDate(requireNotNull(date))
    }

    actual fun format(month: Month): String {
        val formatter = NSDateFormatter().apply {
            val format = when (dateFormatStyle) {
                DateFormatStyle.SHORT -> "M"
                DateFormatStyle.MEDIUM -> "MM"
                DateFormatStyle.LONG -> "MMM"
                DateFormatStyle.FULL -> "MMMM"
            }
            setLocalizedDateFormatFromTemplate(format)
        }
        val dateComponents = NSDateComponents().apply {
            this.month = month.ordinal + 1L
        }
        val date = NSCalendar.currentCalendar().dateFromComponents(dateComponents)
        return formatter.stringFromDate(requireNotNull(date))
    }
}
