package com.teobaranga.monica.core.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json

var isStale = false

@ContributesTo(AppScope::class)
@BindingContainer
object NetworkBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }
}
