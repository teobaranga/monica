package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

sealed interface EditContactActivityUiState {

    data object Loading : EditContactActivityUiState

    @Stable
    data class Loaded(
        private val initialDate: LocalDate,
        val participantResults: StateFlow<List<ActivityParticipant>>,
    ) : EditContactActivityUiState {

        var date: LocalDate by mutableStateOf(initialDate)

        val participantSearch = TextFieldState()

        val participants = mutableStateListOf<ActivityParticipant.Contact>()

        val summary = TextFieldState()

        val details = TextFieldState()
    }
}
