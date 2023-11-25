package com.teobaranga.monica.journal.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.teobaranga.monica.journal.data.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
internal class JournalEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    journalRepository: JournalRepository,
) : ViewModel() {

    val entry = with(savedStateHandle) {
        // ComposeDestinations serializes nulls as Bytes so it's not safe to always ask for an Int
        val entryId = get<Any>("entryId") as? Int? ?: return@with emptyFlow()
        journalRepository.getJournalEntry(entryId)
    }
}
