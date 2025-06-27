package com.teobaranga.monica.log

import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.Logger
import com.diamondedge.logging.PlatformLogger
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(scope = AppScope::class, replaces = [LogComponent::class])
interface TestLogComponent {

    @Provides
    @IntoSet
    @SingleIn(AppScope::class)
    fun provideLogger(): Logger = PlatformLogger(FixedLogLevel(true))
}
