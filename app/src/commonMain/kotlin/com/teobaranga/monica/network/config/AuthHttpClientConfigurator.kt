package com.teobaranga.monica.network.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.account.settings.getTokenStorage
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
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
