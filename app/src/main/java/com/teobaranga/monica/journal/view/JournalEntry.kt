package com.teobaranga.monica.journal.view

import JournalNavGraph
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.journal.view.ui.JournalEntryScreen
import com.teobaranga.monica.journal.view.ui.JournalEntryTopAppBar
import com.teobaranga.monica.ui.navigation.FadeDestinationStyle

@Destination<JournalNavGraph>(
    style = FadeDestinationStyle::class,
)
@Composable
internal fun JournalEntry(
    navigator: DestinationsNavigator,
    entryId: Int? = null,
    viewModel: JournalEntryViewModel = hiltViewModel<JournalEntryViewModel, JournalEntryViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(entryId)
        },
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    JournalEntryScreen(
        uiState = uiState,
        topBar = {
            JournalEntryTopAppBar(
                onBack = navigator::popBackStack,
                onDelete = {
                    viewModel.onDelete()
                    navigator.popBackStack()
                },
            )
        },
        onSave = {
            viewModel.onSave()
            navigator.popBackStack()
        },
    )
}
