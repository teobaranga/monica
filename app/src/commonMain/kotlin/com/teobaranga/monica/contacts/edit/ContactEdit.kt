package com.teobaranga.monica.contacts.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.contacts.edit.ui.ContactEditTopAppBar
import com.teobaranga.monica.core.ui.navigation.LocalNavigator

@Composable
internal fun ContactEdit(
    viewModel: ContactEditViewModel = injectedViewModel<ContactEditViewModel, ContactEditViewModel.Factory>(
        creationCallback = { factory ->
            factory(createSavedStateHandle())
        },
    )
) {
    val navigator = LocalNavigator.current
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
