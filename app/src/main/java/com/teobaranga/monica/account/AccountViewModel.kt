package com.teobaranga.monica.account

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.MonicaDatabase
import com.teobaranga.monica.settings.tokenStorage
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class AccountViewModel(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    private val database: MonicaDatabase,
) : ViewModel() {

    fun onClearAuthorization() {
        viewModelScope.launch(dispatcher.io) {
            // TODO Revoke current access token
            dataStore.edit { preferences ->
                preferences.tokenStorage {
                    clear()
                }
            }
            database.clearAllTables()
        }
    }
}
