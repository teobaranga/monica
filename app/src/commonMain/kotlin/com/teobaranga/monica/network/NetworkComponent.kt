package com.teobaranga.monica.network

import com.teobaranga.monica.network.config.HttpClientConfigurator
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface NetworkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(configurators: Set<HttpClientConfigurator>): HttpClient {
        return HttpClient {
            for (configurator in configurators) {
                with(configurator) {
                    configure()
                }
            }
        }
    }
}
