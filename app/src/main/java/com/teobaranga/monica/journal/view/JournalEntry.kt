package com.teobaranga.monica.journal.view

import JournalNavGraph
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.teobaranga.monica.journal.view.ui.JournalEntryScreen

@JournalNavGraph
@Destination
@Composable
fun JournalEntry(
    entryId: Int? = null,
) {
    val viewModel = hiltViewModel<JournalEntryViewModel>()
    val entry by viewModel.entry.collectAsState(null)
    JournalEntryScreen(
        entry = entry,
    )
}
