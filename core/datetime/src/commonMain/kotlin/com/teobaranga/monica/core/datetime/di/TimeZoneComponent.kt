package com.teobaranga.monica.core.datetime.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.datetime.TimeZone

@ContributesTo(AppScope::class)
interface TimeZoneComponent {

    @Provides
    fun timeZone(): TimeZone = TimeZone.currentSystemDefault()
}
