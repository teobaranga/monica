package com.teobaranga.monica.contacts.detail.activities.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection

data class ContactInfoActivitiesSection(
    private val contactId: Int,
) : ContactInfoSection {

    override val title: String = "Activities"

    @Composable
    override fun Content(modifier: Modifier) {
        val viewModel = hiltViewModel<ContactActivitiesViewModel, ContactActivitiesViewModel.Factory>(
            creationCallback = { factory: ContactActivitiesViewModel.Factory ->
                factory.create(contactId)
            },
        )
        val activities by viewModel.contactActivities.collectAsStateWithLifecycle()
        Crossfade(
            targetState = activities,
            label = "Contact Activities",
        ) { uiState ->
            when (uiState) {
                is ContactActivitiesUiState.Loading -> {
                    ContactActivityPlaceholder()
                }

                is ContactActivitiesUiState.Empty -> {
                    ContactActivityEmpty(
                        modifier = Modifier
                            .padding(top = 60.dp),
                    )
                }

                is ContactActivitiesUiState.Loaded -> {
                    ContactActivitiesColumn(
                        uiState = uiState,
                    )
                }
            }
        }
    }
}
