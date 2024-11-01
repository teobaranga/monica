package com.teobaranga.monica.journal.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.view.ui.JournalEntryUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(assistedFactory = JournalEntryViewModel.Factory::class)
internal class JournalEntryViewModel @AssistedInject constructor(
    @Assisted
    private val entryId: Int?,
    private val dispatcher: Dispatcher,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    val uiState = flow<JournalEntryUiState> {
        val entry = if (entryId == null) {
            null
        } else {
            journalRepository.getJournalEntry(entryId).firstOrNull()
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
            journalRepository.upsertJournalEntry(
                entryId = entryId,
                title = uiState.title.text.toString().takeUnless { it.isBlank() },
                post = uiState.post.text.toString(),
                date = uiState.date,
            )
        }
    }

    fun onDelete() {
        if (entryId == null) {
            return
        }
        viewModelScope.launch(dispatcher.io) {
            journalRepository.deleteJournalEntry(entryId)
        }
    }

    private fun getEmptyState(): JournalEntryUiState.Loaded {
        return JournalEntryUiState.Loaded(
            id = -1,
            initialTitle = null,
            initialPost = "",
            initialDate = LocalDate.now(),
        )
    }

    private fun getLoadedState(): JournalEntryUiState.Loaded? {
        return uiState.value as? JournalEntryUiState.Loaded
    }

    @AssistedFactory
    interface Factory {
        fun create(entryId: Int?): JournalEntryViewModel
    }
}
