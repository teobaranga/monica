package com.teobaranga.monica.journal.view.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

sealed interface JournalEntryUiState {

    data object Loading : JournalEntryUiState

    @Stable
    class Loaded(
        val id: Int,
        initialTitle: String?,
        initialPost: String,
        initialDate: LocalDate,
    ) : JournalEntryUiState {

        var date by mutableStateOf(initialDate)

        val title = TextFieldState(initialTitle.orEmpty())

        val post = TextFieldState(initialPost)

        val hasChanges by derivedStateOf {
            initialTitle.orEmpty() != title.text ||
                initialPost != post.text ||
                initialDate != date
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Loaded

            if (id != other.id) return false
            if (title.text != other.title.text) return false
            if (post.text != other.post.text) return false
            if (date != other.date) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + title.hashCode()
            result = 31 * result + post.hashCode()
            result = 31 * result + date.hashCode()
            return result
        }

        override fun toString(): String {
            return "Loaded(id=$id, date=$date, title=$title, post=$post, hasChanges=$hasChanges)"
        }
    }
}
