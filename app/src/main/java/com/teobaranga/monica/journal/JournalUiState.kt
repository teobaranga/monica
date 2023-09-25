package com.teobaranga.monica.journal

import com.teobaranga.monica.journal.model.JournalEntry

data class JournalUiState(
    val entries: List<JournalEntry>,
)
