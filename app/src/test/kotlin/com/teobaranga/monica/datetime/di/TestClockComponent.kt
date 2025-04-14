package com.teobaranga.monica.datetime.di

import com.teobaranga.monica.core.datetime.di.ClockComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

val testNow = Instant.parse("2025-01-01T00:00:00Z")

@ContributesTo(
    scope = AppScope::class,
    replaces = [ClockComponent::class],
)
interface TestClockComponent {

    @Provides
    fun clock(): Clock {
        return object : Clock {
            override fun now(): Instant {
                return testNow
            }
        }
    }
}
