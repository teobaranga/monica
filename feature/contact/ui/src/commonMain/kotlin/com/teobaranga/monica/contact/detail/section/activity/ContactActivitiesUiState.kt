package com.teobaranga.monica.contact.detail.section.activity

import androidx.compose.runtime.Immutable

sealed interface ContactActivitiesUiState {

    data object Loading : ContactActivitiesUiState

    data object Empty : ContactActivitiesUiState

    @Immutable
    data class Loaded(
        val activities: List<ContactActivity>,
    ) : ContactActivitiesUiState
}
