package com.teobaranga.monica.core.network

import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient

@ContributesTo(AppScope::class)
interface IosNetworkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(
        configurators: Set<HttpClientConfigurator>,
    ): HttpClient {
        return HttpClient {
            for (configurator in configurators) {
                with(configurator) {
                    configure()
                }
            }
        }
    }
}
