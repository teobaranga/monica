package com.teobaranga.monica.network.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import timber.log.Timber

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
class LoggingHttpClientConfigurator: HttpClientConfigurator {

    override fun HttpClientConfig<*>.configure() {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.d("HttpLogging: %s", message)
                }
            }
            level = LogLevel.BODY
        }
    }
}
