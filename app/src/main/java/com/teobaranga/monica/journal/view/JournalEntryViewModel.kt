package com.teobaranga.monica.journal.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.model.JournalEntryUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.OffsetDateTime

@HiltViewModel(assistedFactory = JournalEntryViewModel.Factory::class)
internal class JournalEntryViewModel @AssistedInject constructor(
    journalRepository: JournalRepository,
    @Assisted
    private val entryId: Int?,
) : ViewModel() {

    val entry = when (entryId) {
        null -> flowOf(getEmptyEntry())
        else -> journalRepository.getJournalEntry(entryId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = getEmptyEntry(),
    )

    private fun getEmptyEntry(): JournalEntryUiState {
        return JournalEntryUiState(
            id = -1,
            title = null,
            post = "",
            date = OffsetDateTime.now(),
            created = null,
            updated = null,
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(entryId: Int?): JournalEntryViewModel
    }
}
