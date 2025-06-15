package com.teobaranga.monica.log

import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.Logger
import com.diamondedge.logging.PlatformLogger
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.experimental.ExperimentalNativeApi

@ContributesTo(AppScope::class)
interface LogComponent {

    @OptIn(ExperimentalNativeApi::class)
    @Provides
    @IntoSet
    @SingleIn(AppScope::class)
    fun provideLogger(): Logger = PlatformLogger(FixedLogLevel(Platform.isDebugBinary))
}
