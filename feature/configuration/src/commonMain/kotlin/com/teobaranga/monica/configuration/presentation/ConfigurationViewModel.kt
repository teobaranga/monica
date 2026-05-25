package com.teobaranga.monica.configuration.presentation

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.component.tips.TipsRepository
import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationItem
import com.teobaranga.monica.configuration.domain.RestartAppUseCase
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Inject
@ContributesIntoMap(AppScope::class)
@ViewModelKey
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
