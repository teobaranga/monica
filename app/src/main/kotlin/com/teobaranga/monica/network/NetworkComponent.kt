package com.teobaranga.monica.network

import com.teobaranga.monica.network.config.HttpClientConfigurator
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

lateinit var httpClient: HttpClient

var isStale = false

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
    fun provideHttpClient(sslSettings: SslSettings, configurators: Set<HttpClientConfigurator>): HttpClient {
        if (isStale || !::httpClient.isInitialized) {
            httpClient = HttpClient(OkHttp) {
                engine {
                    config {
                        sslSocketFactory(sslSettings.getSslContext().socketFactory, sslSettings.getTrustManager())
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
