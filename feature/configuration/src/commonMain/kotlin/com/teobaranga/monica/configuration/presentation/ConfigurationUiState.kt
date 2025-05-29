package com.teobaranga.monica.configuration.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class ConfigurationUiState {

    var shouldClearDatabaseOnNextLaunch by mutableStateOf(false)
}
