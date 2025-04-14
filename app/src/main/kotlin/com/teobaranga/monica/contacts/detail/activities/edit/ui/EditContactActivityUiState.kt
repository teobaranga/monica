package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

sealed interface EditContactActivityUiState {

    data object Loading : EditContactActivityUiState

    @Stable
    data class Loaded(
        val participantResults: StateFlow<List<ActivityParticipant>>,
    ) : EditContactActivityUiState {

        var date: LocalDate by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)

        val participantSearch = TextFieldState()

        val participants = mutableStateListOf<ActivityParticipant.Contact>()

        val summary = TextFieldState()

        val details = TextFieldState()
    }
}
