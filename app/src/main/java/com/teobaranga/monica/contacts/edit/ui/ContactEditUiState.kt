package com.teobaranga.monica.contacts.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.teobaranga.monica.contacts.ui.Birthday

sealed interface ContactEditUiState {
    data object Loading : ContactEditUiState

    @Stable
    data class Loaded(
        val id: Int,
        val firstName: TextFieldState,
        val lastName: TextFieldState,
        val nickname: TextFieldState,
        private val initialBirthday: Birthday?,
    ) : ContactEditUiState {

        var birthday by mutableStateOf(initialBirthday)

        constructor(
            id: Int,
            firstName: String,
            lastName: String?,
            nickname: String?,
            initialBirthday: Birthday?,
        ): this(
            id,
            TextFieldState(firstName),
            TextFieldState(lastName.orEmpty()),
            TextFieldState(nickname.orEmpty()),
            initialBirthday,
        )
    }
}
