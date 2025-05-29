package com.teobaranga.monica.core.datetime

fun YearMonth.toJavaYearMonth(): java.time.YearMonth {
    return java.time.YearMonth.of(year, month.value)
}
