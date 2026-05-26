package com.teobaranga.monica.log

import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.Logger
import com.diamondedge.logging.PlatformLogger
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface LogComponent {

    @Provides
    @IntoSet
    @SingleIn(AppScope::class)
    fun provideLogger(): Logger = PlatformLogger(FixedLogLevel(true))
}
