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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = JournalEntryViewModel.Factory::class)
internal class JournalEntryViewModel @AssistedInject constructor(
    @Assisted
    private val entryId: Int?,
    private val dispatcher: Dispatcher,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    val uiState = when (entryId) {
        null -> flowOf(getEmptyState())
        else -> journalRepository.getJournalEntry(entryId)
            .mapLatest { entry ->
                JournalEntryUiState.Loaded(
                    id = entry.id,
                    title = entry.title,
                    post = entry.post,
                    date = entry.date,
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = JournalEntryUiState.Loading,
    )

    fun onSave() {
        viewModelScope.launch(dispatcher.io) {
            val uiState = uiState.value as? JournalEntryUiState.Loaded ?: return@launch
            journalRepository.createJournalEntry(
                title = uiState.title.text.toString().takeUnless { it.isBlank() },
                post = uiState.post.text.toString(),
                date = uiState.date,
            )
        }
    }

    private fun getEmptyState(): JournalEntryUiState.Loaded {
        return JournalEntryUiState.Loaded(
            id = -1,
            title = null,
            post = "",
            date = OffsetDateTime.now(),
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(entryId: Int?): JournalEntryViewModel
    }
}
