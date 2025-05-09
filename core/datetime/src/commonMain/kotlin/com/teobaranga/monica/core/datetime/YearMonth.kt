package com.teobaranga.monica.core.datetime

import kotlinx.datetime.Month

class YearMonth private constructor(
    val year: Year,
    val month: Month,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as YearMonth

        if (year != other.year) return false
        if (month != other.month) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month.hashCode()
        return result
    }

    override fun toString(): String {
        return "YearMonth(year=$year, month=$month)"
    }

    companion object {

        fun of(year: Year, month: Month): YearMonth {
            require(year >= 0) { "Year must be positive" }
            return YearMonth(year, month)
        }
    }
}
