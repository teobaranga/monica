package com.teobaranga.monica.core.network

import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

lateinit var httpClient: HttpClient

@ContributesTo(AppScope::class)
@BindingContainer
object AndroidNetworkBindings {

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
