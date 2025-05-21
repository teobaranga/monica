package com.teobaranga.monica.core.datetime

fun MonthDay.toJavaMonthDay(): java.time.MonthDay {
    return java.time.MonthDay.of(month.value, dayOfMonth)
}
