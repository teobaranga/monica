package com.teobaranga.monica.dashboard

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.auth.AuthorizationRepository
import com.teobaranga.monica.settings.oAuthSettings
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    authorizationRepository: AuthorizationRepository,
) : ViewModel() {

    val isLoggedIn = authorizationRepository.isLoggedIn

    fun onClearAuthorization() {
        viewModelScope.launch(dispatcher.io) {
            dataStore.edit { preferences ->
                preferences.oAuthSettings {
                    removeAuthorizationCode()
                }
            }
        }
    }
}
