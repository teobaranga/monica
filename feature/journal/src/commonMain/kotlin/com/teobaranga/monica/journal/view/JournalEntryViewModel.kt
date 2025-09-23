package com.teobaranga.monica.journal.view

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.component.tips.TipsRepository
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.data.JournalTips
import com.teobaranga.monica.journal.view.ui.JournalEntryError
import com.teobaranga.monica.journal.view.ui.JournalEntryUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(scope = AppScope::class, assistedFactory = JournalEntryViewModel.Factory::class)
class JournalEntryViewModel(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val journalRepository: JournalRepository,
    private val getNowLocalDate: () -> LocalDate,
    private val tipsRepository: TipsRepository,
) : ViewModel() {

    val journalEntryRoute = savedStateHandle.toRoute<JournalEntryRoute>()

    val uiState = flow<JournalEntryUiState> {
        val entry = if (journalEntryRoute.entryId == null) {
            null
        } else {
            journalRepository.getJournalEntry(journalEntryRoute.entryId).firstOrNull()
        }
        val uiState = if (entry == null) {
            getEmptyState()
        } else {
            JournalEntryUiState.Loaded(
                id = entry.id,
                initialTitle = entry.title,
                initialPost = entry.post,
                initialDate = entry.date,
                showTitleBugInfo = !tipsRepository.isTipSeen(JournalTips.mandatoryTitleServerBug),
            )
        }
        emit(uiState)
    }.onEach { state ->
        if (state is JournalEntryUiState.Loaded) {
            postJob?.cancel()
            postJob = viewModelScope.launch {
                snapshotFlow { state.post.text }
                    .distinctUntilChanged()
                    .onEach {
                        state.postError = null
                    }
                    .collect()
            }
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = JournalEntryUiState.Loading,
        started = SharingStarted.Eagerly,
    )

    private val _effects = MutableSharedFlow<JournalEntryEffect>()
    val effects = _effects.asSharedFlow()

    private var postJob: Job? = null

    fun onSave() {
        viewModelScope.launch {
            val uiState = getLoadedState() ?: return@launch
            if (uiState.post.text.isBlank()) {
                uiState.postError = JournalEntryError.Empty
                return@launch
            }
            journalRepository.upsertJournalEntry(
                entryId = journalEntryRoute.entryId,
                title = uiState.title.text.toString().takeUnless { it.isBlank() },
                post = uiState.post.text.toString(),
                date = uiState.date,
            )
            _effects.emit(JournalEntryEffect.Back)
        }
    }

    fun onDelete() {
        if (journalEntryRoute.entryId == null) {
            return
        }
        viewModelScope.launch {
            journalRepository.deleteJournalEntry(journalEntryRoute.entryId)
            _effects.emit(JournalEntryEffect.Back)
        }
    }

    fun onTipDismiss(id: String) {
        viewModelScope.launch {
            tipsRepository.markAsSeen(id)
        }
    }

    private suspend fun getEmptyState(): JournalEntryUiState.Loaded {
        return JournalEntryUiState.Loaded(
            id = -1,
            initialTitle = null,
            initialPost = "",
            initialDate = getNowLocalDate(),
            showTitleBugInfo = !tipsRepository.isTipSeen(JournalTips.mandatoryTitleServerBug),
        )
    }

    private fun getLoadedState(): JournalEntryUiState.Loaded? {
        return uiState.value as? JournalEntryUiState.Loaded
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): JournalEntryViewModel
    }
}
