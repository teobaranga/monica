package com.teobaranga.monica.journal.view

import JournalNavGraph
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
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
    viewModel: JournalEntryViewModel = injectedViewModel<JournalEntryViewModel, JournalEntryViewModelFactory>(
        creationCallback = { factory ->
            factory(entryId)
        },
    ),
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    JournalEntryScreen(
        uiState = uiState,
        topBar = {
            JournalEntryTopAppBar(
                onBack = {
                    onBackPressedDispatcher?.onBackPressed()
                },
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
