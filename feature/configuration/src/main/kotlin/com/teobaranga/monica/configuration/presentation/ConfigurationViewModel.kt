package com.teobaranga.monica.configuration.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationItem
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val configurationDataStore: ConfigurationDataStore,
) : ViewModel() {

    val uiState = ConfigurationUiState()

    init {
        with(viewModelScope) {
            launch(dispatcher.io) {
                configurationDataStore.getAsFlow(ConfigurationItem.ShouldClearDatabaseOnNextLaunch)
                    .collectLatest {
                        uiState.shouldClearDatabaseOnNextLaunch = it
                    }
            }
            launch(dispatcher.io) {
                snapshotFlow { uiState.shouldClearDatabaseOnNextLaunch }
                    .drop(1)
                    .collectLatest {
                        configurationDataStore.set(ConfigurationItem.ShouldClearDatabaseOnNextLaunch, it)
                    }
            }
        }
    }
}
