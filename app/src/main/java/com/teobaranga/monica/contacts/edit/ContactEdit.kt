package com.teobaranga.monica.contacts.edit

import ContactsNavGraph
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.contacts.edit.ui.ContactEditTopAppBar

@Destination<ContactsNavGraph>
@Composable
internal fun ContactEdit(
    navigator: DestinationsNavigator,
    contactId: Int? = null,
    viewModel: ContactEditViewModel = hiltViewModel<ContactEditViewModel, ContactEditViewModel.Factory>(
        creationCallback = { factory: ContactEditViewModel.Factory ->
            factory.create(contactId)
        },
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ContactEditScreen(
        uiState = uiState,
        topBar = {
            ContactEditTopAppBar(
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
