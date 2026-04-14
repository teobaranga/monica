package com.teobaranga.monica.core.test

import kotlin.time.Clock
import kotlin.time.Instant

data class FixedClock(private val now: Instant): Clock {

    override fun now(): Instant = now
}
