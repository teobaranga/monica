package com.teobaranga.monica.core.test

import com.teobaranga.monica.core.datetime.di.ClockBindings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.time.Clock
import kotlin.time.Instant

val testNow = Instant.parse("2025-01-01T00:00:00Z")

@ContributesTo(
    scope = AppScope::class,
    replaces = [ClockBindings::class],
)
@BindingContainer
object TestClockBindings {

    @Provides
    fun clock(): Clock {
        return FixedClock(testNow)
    }
}
