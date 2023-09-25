package com.teobaranga.monica.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.journal.data.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class JournalViewModel @Inject constructor(
    journalRepository: JournalRepository,
) : ViewModel() {

    val uiState = journalRepository.getJournalEntries(orderBy = JournalRepository.OrderBy.Date(isAscending = false))
        .mapLatest { journalEntries ->
            JournalUiState(
                entries = journalEntries,
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}
