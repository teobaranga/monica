package com.teobaranga.monica.core.datetime.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.time.Clock

@ContributesTo(AppScope::class)
interface ClockComponent {

    @Provides
    fun clock(): Clock = Clock.System
}
