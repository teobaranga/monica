package com.teobaranga.monica.journal.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.view.ui.JournalEntryUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class JournalEntryViewModel(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val dispatcher: Dispatcher,
    private val journalRepository: JournalRepository,
    private val getNowLocalDate: () -> LocalDate,
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
            )
        }
        emit(uiState)
    }.stateIn(
        scope = viewModelScope,
        initialValue = JournalEntryUiState.Loading,
        started = SharingStarted.Eagerly,
    )

    fun onSave() {
        viewModelScope.launch(dispatcher.io) {
            val uiState = getLoadedState() ?: return@launch
            if (uiState.post.text.isBlank()) {
                // TODO: show error
                return@launch
            }
            journalRepository.upsertJournalEntry(
                entryId = journalEntryRoute.entryId,
                title = uiState.title.text.toString().takeUnless { it.isBlank() },
                post = uiState.post.text.toString(),
                date = uiState.date,
            )
        }
    }

    fun onDelete() {
        if (journalEntryRoute.entryId == null) {
            return
        }
        viewModelScope.launch(dispatcher.io) {
            journalRepository.deleteJournalEntry(journalEntryRoute.entryId)
        }
    }

    private fun getEmptyState(): JournalEntryUiState.Loaded {
        return JournalEntryUiState.Loaded(
            id = -1,
            initialTitle = null,
            initialPost = "",
            initialDate = getNowLocalDate(),
        )
    }

    private fun getLoadedState(): JournalEntryUiState.Loaded? {
        return uiState.value as? JournalEntryUiState.Loaded
    }
}
