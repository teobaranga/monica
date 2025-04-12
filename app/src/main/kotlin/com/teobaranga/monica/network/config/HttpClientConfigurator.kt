package com.teobaranga.monica.network.config

import io.ktor.client.HttpClientConfig

interface HttpClientConfigurator {

    fun HttpClientConfig<*>.configure()
}
