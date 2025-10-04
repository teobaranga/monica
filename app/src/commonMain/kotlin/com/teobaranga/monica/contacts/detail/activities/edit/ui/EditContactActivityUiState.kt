package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.annotation.RememberInComposition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.datetime.LocalDate

sealed interface EditContactActivityUiState {

    data object Loading : EditContactActivityUiState

    @Stable
    class Loaded(initialDate: LocalDate) : EditContactActivityUiState {

        var date by mutableStateOf(initialDate)

        val participantsState = ParticipantsState()

        val summary = TextFieldState()

        val details = TextFieldState()
    }
}

@Stable
class ParticipantsState @RememberInComposition constructor() {

    val participantSearch = TextFieldState()

    val suggestions = mutableStateListOf<ActivityParticipant>()

    val participants = mutableStateListOf<ActivityParticipant.Contact>()

    override fun toString(): String {
        return Snapshot.withoutReadObservation {
            "ParticipantsState(" +
                "participantSearch=\"$participantSearch\", " +
                "suggestions=$suggestions, " +
                "participants=$participants)"
        }
    }
}
