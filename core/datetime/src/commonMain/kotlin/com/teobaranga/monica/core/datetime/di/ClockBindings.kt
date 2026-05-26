package com.teobaranga.monica.core.datetime.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.time.Clock

@ContributesTo(AppScope::class)
@BindingContainer
object ClockBindings {

    @Provides
    fun clock(): Clock = Clock.System
}
