package com.teobaranga.monica.journal.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.core.ui.ConfirmExitDialog
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.journal.view.ui.JournalEntryScreen
import com.teobaranga.monica.journal.view.ui.JournalEntryTopAppBar
import com.teobaranga.monica.journal.view.ui.JournalEntryUiState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun JournalEntry(
    viewModel: JournalEntryViewModel = injectedViewModel<JournalEntryViewModel, JournalEntryViewModel.Factory>(
        creationCallback = { factory ->
            factory(createSavedStateHandle())
        },
    ),
) {
    val navigator = LocalNavigator.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isConfirming by remember { mutableStateOf(false) }

    BackHandler(uiState.hasUnsavedChanges) {
        isConfirming = true
    }

    JournalEntryEffectHandler(
        effects = viewModel.effects,
    )

    JournalEntryScreen(
        uiState = uiState,
        topBar = {
            JournalEntryTopAppBar(
                onBack = {
                    if (uiState.hasUnsavedChanges) {
                        isConfirming = true
                    } else {
                        navigator.popBackStack()
                    }
                },
                onDelete = if (viewModel.journalEntryRoute.entryId != null) {
                    viewModel::onDelete
                } else {
                    null
                },
            )
        },
        onSave = viewModel::onSave,
        onTipDismiss = viewModel::onTipDismiss,
    )

    if (isConfirming) {
        ConfirmExitDialog(
            title = "Unsaved changes",
            text = "You have unsaved changes. Are you sure you want to exit?",
            confirmText = "Keep editing",
            onConfirm = {
                isConfirming = false
            },
            dismissText = "Exit",
            onDismiss = {
                isConfirming = false
                navigator.popBackStack()
            },
        )
    }
}

private val JournalEntryUiState.hasUnsavedChanges
    get() = (this as? JournalEntryUiState.Loaded)?.hasChanges ?: false
