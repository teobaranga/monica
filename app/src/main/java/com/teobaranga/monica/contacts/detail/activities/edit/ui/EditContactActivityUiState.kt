package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

sealed interface EditContactActivityUiState {

    data object Loading : EditContactActivityUiState

    @Stable
    data class Loaded(
        private val onParticipantSearch: (String) -> Unit,
        val participantResults: StateFlow<List<ActivityParticipant>>,
    ) : EditContactActivityUiState {

        var date: LocalDate by mutableStateOf(LocalDate.now())

        var participantSearch by mutableStateOf(TextFieldValue())
            private set

        val participants = mutableStateListOf<ActivityParticipant.Contact>()

        var summary by mutableStateOf(TextFieldValue())

        var details by mutableStateOf(TextFieldValue())

        fun onParticipantSearch(search: TextFieldValue) {
            participantSearch = search
            onParticipantSearch(search.text)
        }
    }
}
