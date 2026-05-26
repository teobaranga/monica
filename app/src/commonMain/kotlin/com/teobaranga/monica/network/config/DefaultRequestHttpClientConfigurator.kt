package com.teobaranga.monica.network.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.inject.ApplicationContext
import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import com.teobaranga.monica.settings.getOAuthSettings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

@Inject
@SingleIn(AppScope::class)
@ContributesIntoSet(AppScope::class)
class DefaultRequestHttpClientConfigurator(
    @ApplicationContext
    appCoroutineScope: CoroutineScope,
    dispatcher: Dispatcher,
    dataStore: DataStore<Preferences>,
) : HttpClientConfigurator {

    @Volatile
    private lateinit var currentUrl: String

    init {
        appCoroutineScope.launch(dispatcher.io) {
            dataStore.data
                .mapLatest { preferences ->
                    preferences.getOAuthSettings().serverAddress
                }
                .filterNotNull()
                .collectLatest { serverAddress ->
                    currentUrl = serverAddress
                }
        }
    }

    override fun HttpClientConfig<*>.configure() {
        defaultRequest {
            contentType(ContentType.Application.Json)
            url(currentUrl)
        }
    }
}
