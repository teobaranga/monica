package com.teobaranga.monica.core.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MonthDay private constructor(
    val month: Month,
    val dayOfMonth: Int,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MonthDay

        if (dayOfMonth != other.dayOfMonth) return false
        if (month != other.month) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dayOfMonth
        result = 31 * result + month.hashCode()
        return result
    }

    override fun toString(): String {
        return "MonthDay(month=$month, dayOfMonth=$dayOfMonth)"
    }

    companion object {

        private const val MAX_DAY = 31

        fun of(month: Month, day: Int): MonthDay {
            require(day in 1..MAX_DAY) { "Day must be between 1 and $MAX_DAY" }
            return MonthDay(month, day)
        }

        fun from(localDate: LocalDate): MonthDay {
            return MonthDay(
                month = localDate.month,
                dayOfMonth = localDate.dayOfMonth,
            )
        }
    }
}

fun Clock.todayMonthDay(timeZone: TimeZone): MonthDay {
    val localDate = now().toLocalDateTime(timeZone).date
    return MonthDay.from(localDate)
}
