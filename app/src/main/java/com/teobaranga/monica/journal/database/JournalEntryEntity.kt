package com.teobaranga.monica.journal.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.journal.model.JournalEntry
import java.time.ZonedDateTime
import java.util.UUID

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey
    val id: Int,
    val uuid: UUID,
    val accountId: Int,
    val title: String?,
    val post: String,
    val date: ZonedDateTime,
    val created: ZonedDateTime,
    val updated: ZonedDateTime,
)

fun JournalEntryEntity.toExternalModel(): JournalEntry {
    return JournalEntry(
        id = id,
        title = title,
        post = post,
        date = date,
        created = created,
        updated = updated,
    )
}
