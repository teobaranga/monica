package com.teobaranga.monica.configuration.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.component.tips.TipsRepository
import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationItem
import com.teobaranga.monica.configuration.domain.RestartAppUseCase
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class ConfigurationViewModel(
    private val dispatcher: Dispatcher,
    private val configurationDataStore: ConfigurationDataStore,
    private val restartApp: RestartAppUseCase,
    private val tipsRepository: TipsRepository,
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

    fun onRestartApp() {
        restartApp()
    }

    fun resetTips() {
        viewModelScope.launch {
            tipsRepository.deleteAll()
        }
    }
}
