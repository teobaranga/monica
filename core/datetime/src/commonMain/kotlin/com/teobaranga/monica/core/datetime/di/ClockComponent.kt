package com.teobaranga.monica.core.datetime.di

import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import kotlin.time.Clock

@ContributesTo(AppScope::class)
interface ClockComponent {

    @Provides
    fun clock(): Clock = Clock.System
}
