package com.teobaranga.monica.core.test

import com.teobaranga.monica.core.network.NetworkComponent
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(scope = AppScope::class, replaces = [NetworkComponent::class])
interface TestNetworkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }
}
