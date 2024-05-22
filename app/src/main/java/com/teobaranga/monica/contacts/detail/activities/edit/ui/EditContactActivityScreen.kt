package com.teobaranga.monica.contacts.detail.activities.edit.ui

import ContactsNavGraph
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<ContactsNavGraph>
@Composable
internal fun EditContactActivity(
    navigator: DestinationsNavigator,
    contactId: Int,
    viewModel: EditContactActivityViewModel = hiltViewModel<EditContactActivityViewModel, EditContactActivityViewModel.Factory>(
        creationCallback = { factory: EditContactActivityViewModel.Factory ->
            factory.create(contactId)
        },
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Crossfade(
        targetState = uiState,
        label = "Edit Contact Activity Screen",
    ) { uiState ->
        when (uiState) {
            is EditContactActivityUiState.Loading -> {
                // TODO Loading
            }

            is EditContactActivityUiState.Loaded -> {

            }
        }
    }
}
