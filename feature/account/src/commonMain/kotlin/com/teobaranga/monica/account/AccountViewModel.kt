package com.teobaranga.monica.account

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.account.settings.tokenStorage
import com.teobaranga.monica.core.dispatcher.Dispatcher
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class AccountViewModel(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    private val database: RoomDatabase,
    private val httpClient: () -> HttpClient,
) : ViewModel() {

    fun onClearAuthorization() {
        viewModelScope.launch(dispatcher.io) {
            // TODO Revoke current access token
            httpClient().authProvider<BearerAuthProvider>()?.clearToken()

            dataStore.edit { preferences ->
                preferences.tokenStorage {
                    clear()
                }
            }
            database.clear()
        }
    }
}

/**
 * Clear all tables in the database.
 */
expect fun RoomDatabase.clear()
