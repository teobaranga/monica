package com.teobaranga.monica.journal.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import java.time.OffsetDateTime

@Stable
data class JournalEntryUiState(
    val id: Int,
    val title: TextFieldState,
    val post: TextFieldState,
    val date: OffsetDateTime,
    val created: OffsetDateTime?,
    val updated: OffsetDateTime?,
) {
    constructor(
        id: Int,
        title: String?,
        post: String,
        date: OffsetDateTime,
        created: OffsetDateTime?,
        updated: OffsetDateTime?,
    ) : this(
        id = id,
        title = TextFieldState(title.orEmpty()),
        post = TextFieldState(post),
        date = date,
        created = created,
        updated = updated,
    )
}
