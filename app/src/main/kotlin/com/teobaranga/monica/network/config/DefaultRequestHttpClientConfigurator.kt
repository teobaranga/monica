package com.teobaranga.monica.network.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.inject.ApplicationContext
import com.teobaranga.monica.settings.getOAuthSettings
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
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
