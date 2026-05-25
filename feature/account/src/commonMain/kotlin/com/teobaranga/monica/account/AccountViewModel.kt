package com.teobaranga.monica.account

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.teobaranga.monica.account.settings.tokenStorage
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.launch

@Inject
@ContributesIntoMap(AppScope::class)
@ViewModelKey
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
