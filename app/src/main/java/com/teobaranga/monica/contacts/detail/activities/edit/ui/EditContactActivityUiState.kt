package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDate

@Stable
class EditContactActivityUiState {

    var date by mutableStateOf(LocalDate.now())

    val participants = mutableStateListOf<ActivityParticipant>()

    var summary by mutableStateOf(TextFieldValue())

    var details by mutableStateOf(TextFieldValue())
}
