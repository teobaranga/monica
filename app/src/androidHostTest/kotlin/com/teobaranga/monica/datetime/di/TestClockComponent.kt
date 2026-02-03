package com.teobaranga.monica.datetime.di

import com.teobaranga.monica.core.datetime.di.ClockComponent
import com.teobaranga.monica.datetime.FixedClock
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import kotlin.time.Clock
import kotlin.time.Instant

val testNow = Instant.parse("2025-01-01T00:00:00Z")

@ContributesTo(
    scope = AppScope::class,
    replaces = [ClockComponent::class],
)
interface TestClockComponent {

    @Provides
    fun clock(): Clock {
        return FixedClock(testNow)
    }
}
