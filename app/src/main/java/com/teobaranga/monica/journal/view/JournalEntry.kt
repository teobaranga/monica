package com.teobaranga.monica.journal.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.journal.view.ui.JournalEntryScreen
import com.teobaranga.monica.journal.view.ui.JournalEntryTopAppBar
import com.teobaranga.monica.ui.navigation.LocalNavigator

@Composable
internal fun JournalEntry(
    viewModel: JournalEntryViewModel = injectedViewModel<JournalEntryViewModel>(),
) {
    val navigator = LocalNavigator.current
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
