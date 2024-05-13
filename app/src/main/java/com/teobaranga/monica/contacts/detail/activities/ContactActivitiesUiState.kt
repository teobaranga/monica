package com.teobaranga.monica.contacts.detail.activities

import androidx.compose.runtime.Immutable

sealed interface ContactActivitiesUiState {
    data object Loading : ContactActivitiesUiState

    @Immutable
    data class Loaded(
        val activities: List<ContactActivity>,
    ) : ContactActivitiesUiState
}
