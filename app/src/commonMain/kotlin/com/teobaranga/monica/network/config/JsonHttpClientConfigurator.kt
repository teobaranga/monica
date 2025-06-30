package com.teobaranga.monica.network.config

import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
class JsonHttpClientConfigurator(
    private val json: Json,
) : HttpClientConfigurator {

    override fun HttpClientConfig<*>.configure() {
        install(ContentNegotiation) {
            json(json)
        }
    }
}
