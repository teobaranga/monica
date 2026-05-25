package com.teobaranga.monica.network.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.account.settings.getTokenStorage
import com.teobaranga.monica.core.network.config.HttpClientConfigurator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.coroutines.flow.first

@Inject
@SingleIn(AppScope::class)
@ContributesIntoSet(AppScope::class)
class AuthHttpClientConfigurator(
    private val dataStore: DataStore<Preferences>,
) : HttpClientConfigurator {

    override fun HttpClientConfig<*>.configure() {
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = dataStore.data.first()
                        .getTokenStorage()
                        .accessToken

                    accessToken?.let {
                        BearerTokens(it, null)
                    }
                }
            }
        }
    }
}
