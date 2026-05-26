package com.teobaranga.monica.contact.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teobaranga.monica.contact.nav.ContactEditRoute
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

fun NavGraphBuilder.contactEdit() {
    composable<ContactEditRoute> {
        ContactEdit()
    }
}

@Composable
internal fun ContactEdit(
    viewModel: ContactEditViewModel = assistedMetroViewModel<ContactEditViewModel>(),
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
