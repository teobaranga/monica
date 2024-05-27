package com.teobaranga.monica.journal.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.model.JournalEntryUiState
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

@HiltViewModel(assistedFactory = JournalEntryViewModel.Factory::class)
internal class JournalEntryViewModel @AssistedInject constructor(
    @Assisted
    private val entryId: Int?,
    private val dispatcher: Dispatcher,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    val entry = when (entryId) {
        null -> flowOf(getEmptyEntry())
        else -> journalRepository.getJournalEntry(entryId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = getEmptyEntry(),
    )

    fun onSave() {
        viewModelScope.launch(dispatcher.io) {
            val entry = entry.value
            journalRepository.createJournalEntry(
                title = entry.title.text.toString().takeUnless { it.isBlank() },
                post = entry.post.text.toString(),
                date = entry.date,
            )
        }
    }

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
