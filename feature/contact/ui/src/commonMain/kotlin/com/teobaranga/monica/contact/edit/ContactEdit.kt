package com.teobaranga.monica.contact.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.contact.nav.ContactEditRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

fun EntryProviderScope<NavKey>.contactEdit() {
    entry<ContactEditRoute> { route ->
        ContactEdit(
            viewModel = assistedMetroViewModel<ContactEditViewModel, ContactEditViewModel.Factory>(
                createViewModel = {
                    create(route.contactId, route.contactName)
                },
            ),
        )
    }
}

@Composable
internal fun ContactEdit(
    viewModel: ContactEditViewModel,
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
