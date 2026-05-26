package com.teobaranga.monica.core.test

import com.teobaranga.monica.core.network.NetworkBindings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json

@ContributesTo(scope = AppScope::class, replaces = [NetworkBindings::class])
@BindingContainer
object TestNetworkBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }
}
