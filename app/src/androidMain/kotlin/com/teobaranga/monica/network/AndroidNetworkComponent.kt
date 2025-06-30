package com.teobaranga.monica.network

import com.teobaranga.monica.network.config.HttpClientConfigurator
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

lateinit var httpClient: HttpClient

@ContributesTo(AppScope::class)
interface AndroidNetworkComponent {

    @Provides
    fun provideHttpEngine(): HttpClientEngineFactory<*> = OkHttp

    @Provides
    fun provideHttpClient(
        sslSettings: SslSettings,
        engineFactory: HttpClientEngineFactory<*>,
        configurators: Set<HttpClientConfigurator>,
    ): HttpClient {
        if (isStale || !::httpClient.isInitialized) {
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
