package com.teobaranga.monica

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.destinations.DashboardDestination
import com.teobaranga.monica.destinations.DirectionDestination
import com.teobaranga.monica.destinations.SetupDestination
import com.teobaranga.monica.settings.getOAuthSettings
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    var startDestination: DirectionDestination? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch(dispatcher.io) {
            dataStore.data
                .collectLatest { preferences ->
                    val oAuthSettings = preferences.getOAuthSettings()
                    withContext(dispatcher.main) {
                        startDestination = if (oAuthSettings.authorizationCode == null) {
                            SetupDestination
                        } else {
                            DashboardDestination
                        }
                    }
                }
        }
    }
}
