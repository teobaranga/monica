package com.teobaranga.monica.journal.view.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.OffsetDateTime

sealed interface JournalEntryUiState {
    data object Loading : JournalEntryUiState

    @Stable
    data class Loaded(
        val id: Int,
        val title: TextFieldState,
        val post: TextFieldState,
        private val initialDate: OffsetDateTime,
    ) : JournalEntryUiState {

        var date by mutableStateOf(initialDate)

        constructor(
            id: Int,
            title: String?,
            post: String,
            date: OffsetDateTime,
        ) : this(
            id = id,
            title = TextFieldState(title.orEmpty()),
            post = TextFieldState(post),
            initialDate = date,
        )
    }
}
