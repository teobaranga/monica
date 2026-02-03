package com.teobaranga.monica.datetime

import kotlin.time.Clock
import kotlin.time.Instant

data class FixedClock(private val now: Instant): Clock {

    override fun now(): Instant = now
}
