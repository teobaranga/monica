package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.runtime.Immutable

sealed interface EditContactActivityUiState {
    data object Loading : EditContactActivityUiState

    @Immutable
    data class Loaded(
        val participants: List<ActivityParticipant>,
    ) : EditContactActivityUiState
}
