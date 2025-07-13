package com.teobaranga.monica.core.network

import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

lateinit var httpClient: HttpClient

@ContributesTo(AppScope::class)
interface AndroidNetworkComponent: NetworkComponent {

    @Provides
    fun provideHttpClient(
        sslSettings: SslSettings,
        configurators: Set<HttpClientConfigurator>,
    ): HttpClient {
        if (!::httpClient.isInitialized || isStale) {
            httpClient = HttpClient(OkHttp) {
                engine {
                    config {
                        sslSocketFactory(
                            sslSocketFactory = sslSettings.getSslContext().socketFactory,
                            trustManager = sslSettings.getTrustManager(),
                        )
                    }
                }
                for (configurator in configurators) {
                    with(configurator) {
                        configure()
                    }
                }
            }
            isStale = false
        }
        return httpClient
    }
}
