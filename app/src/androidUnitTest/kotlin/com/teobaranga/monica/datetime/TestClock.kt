package com.teobaranga.monica.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class FixedClock(private val now: Instant): Clock {

    override fun now(): Instant = now
}
