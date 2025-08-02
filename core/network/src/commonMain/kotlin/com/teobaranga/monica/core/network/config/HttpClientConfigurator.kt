package com.teobaranga.monica.core.network.config

import io.ktor.client.HttpClientConfig

interface HttpClientConfigurator {

    fun HttpClientConfig<*>.configure()
}
