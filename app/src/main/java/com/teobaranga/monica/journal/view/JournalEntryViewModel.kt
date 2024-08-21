package com.teobaranga.monica.journal.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.view.ui.JournalEntryUiState
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(assistedFactory = JournalEntryViewModel.Factory::class)
internal class JournalEntryViewModel @AssistedInject constructor(
    @Assisted
    private val entryId: Int?,
    private val dispatcher: Dispatcher,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        when (entryId) {
            null -> getEmptyState()
            else -> JournalEntryUiState.Loading
        }
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher.io) {
            val entry = if (entryId == null) {
                null
            } else {
                journalRepository.getJournalEntry(entryId).firstOrNull()
            }
            _uiState.value = if (entry == null) {
                getEmptyState()
            } else {
                JournalEntryUiState.Loaded(
                    id = entry.id,
                    title = entry.title,
                    post = entry.post,
                    date = entry.date,
                )
            }
        }
    }

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
            title = null,
            post = "",
            date = LocalDate.now(),
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
