package com.teobaranga.monica.journal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class JournalViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    var uiState by mutableStateOf<JournalUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            journalRepository.getJournalEntries(orderBy = JournalRepository.OrderBy.Date(isAscending = false))
                .mapLatest { journalEntries ->
                    JournalUiState(
                        entries = journalEntries,
                    )
                }
                .flowOn(dispatcher.main)
                .collectLatest { journalUiState ->
                    uiState = journalUiState
                }
        }
    }
}
