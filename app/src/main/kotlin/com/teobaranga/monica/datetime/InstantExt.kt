package com.teobaranga.monica.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil

object InstantExt {

    fun Instant.yearsUntilToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Int {
        return yearsUntil(Clock.System.now(), timeZone)
    }

    fun Instant.toSystemLocalDateTime(): LocalDateTime {
        return toLocalDateTime(TimeZone.currentSystemDefault())
    }
}
