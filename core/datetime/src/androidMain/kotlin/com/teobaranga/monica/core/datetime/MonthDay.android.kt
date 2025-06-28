package com.teobaranga.monica.core.datetime

import kotlinx.datetime.toJavaMonth

fun MonthDay.toJavaMonthDay(): java.time.MonthDay {
    return java.time.MonthDay.of(month.toJavaMonth(), dayOfMonth)
}
