package com.teobaranga.monica.network.config

import com.diamondedge.logging.logging
import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

@Inject
@SingleIn(AppScope::class)
@ContributesIntoSet(AppScope::class)
class LoggingHttpClientConfigurator: HttpClientConfigurator {

    override fun HttpClientConfig<*>.configure() {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    log.d { message }
                }
            }
            level = LogLevel.BODY
        }
    }

    companion object {
        private val log = logging("HttpLogging")
    }
}
