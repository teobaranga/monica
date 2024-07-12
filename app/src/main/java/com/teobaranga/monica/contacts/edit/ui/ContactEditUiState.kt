package com.teobaranga.monica.contacts.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

sealed interface ContactEditUiState {
    data object Loading : ContactEditUiState

    @Stable
    data class Loaded(
        val id: Int,
        val firstName: TextFieldState,
        val lastName: TextFieldState,
        val nickname: TextFieldState,
    ) : ContactEditUiState {

        constructor(
            id: Int,
            firstName: String,
            lastName: String?,
            nickname: String?,
        ): this(
            id,
            TextFieldState(firstName),
            TextFieldState(lastName.orEmpty()),
            TextFieldState(nickname.orEmpty()),
        )
    }
}
