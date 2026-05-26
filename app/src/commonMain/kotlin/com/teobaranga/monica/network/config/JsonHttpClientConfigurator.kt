package com.teobaranga.monica.network.config

import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Inject
@SingleIn(AppScope::class)
@ContributesIntoSet(AppScope::class)
class JsonHttpClientConfigurator(
    private val json: Json,
) : HttpClientConfigurator {

    override fun HttpClientConfig<*>.configure() {
        install(ContentNegotiation) {
            json(json)
        }
    }
}
