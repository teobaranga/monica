package com.teobaranga.monica.contacts.detail.activities.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.generated.destinations.EditContactActivityDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection

data class ContactInfoActivitiesSection(
    private val contactId: Int,
) : ContactInfoSection {

    override val title: String = "Activities"

    @Composable
    override fun Content(modifier: Modifier, navigator: DestinationsNavigator) {
        val viewModel = hiltViewModel<ContactActivitiesViewModel, ContactActivitiesViewModel.Factory>(
            creationCallback = { factory: ContactActivitiesViewModel.Factory ->
                factory.create(contactId)
            },
        )
        val activitiesUiState by viewModel.contactActivities.collectAsStateWithLifecycle()
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                if (activitiesUiState != ContactActivitiesUiState.Loading) {
                    FloatingActionButton(
                        modifier = Modifier
                            .navigationBarsPadding(),
                        onClick = {
                            navigator.navigate(EditContactActivityDestination(contactId))
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add activity",
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { contentPadding ->
            Crossfade(
                targetState = activitiesUiState,
                label = "Contact Activities",
            ) { uiState ->
                when (uiState) {
                    is ContactActivitiesUiState.Loading -> {
                        ContactActivityPlaceholder()
                    }

                    is ContactActivitiesUiState.Empty -> {
                        ContactActivityEmpty(
                            modifier = Modifier
                                .padding(contentPadding)
                                .padding(top = 60.dp),
                        )
                    }

                    is ContactActivitiesUiState.Loaded -> {
                        ContactActivitiesColumn(
                            modifier = Modifier
                                .padding(contentPadding),
                            uiState = uiState,
                        )
                    }
                }
            }
        }
    }
}
