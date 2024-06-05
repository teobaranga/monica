package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDate

sealed interface EditContactActivityUiState {

    data object Loading : EditContactActivityUiState

    @Stable
    data class Loaded(
        private val onParticipantSearch: (String) -> Unit,
    ) : EditContactActivityUiState {

        var date by mutableStateOf(LocalDate.now())

        var participantSearch by mutableStateOf(TextFieldValue())
            private set

        val participantResults = mutableStateListOf<ActivityParticipant>()

        val participants = mutableStateListOf<ActivityParticipant>()

        var summary by mutableStateOf(TextFieldValue())

        var details by mutableStateOf(TextFieldValue())

        fun onParticipantSearch(search: TextFieldValue) {
            participantSearch = search
            onParticipantSearch(search.text)
        }
    }
}
