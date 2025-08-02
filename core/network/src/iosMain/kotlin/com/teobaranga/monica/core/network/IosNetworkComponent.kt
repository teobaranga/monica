package com.teobaranga.monica.core.network

import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface IosNetworkComponent: NetworkComponent {

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
