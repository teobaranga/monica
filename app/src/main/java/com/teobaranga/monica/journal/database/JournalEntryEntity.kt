package com.teobaranga.monica.journal.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.data.sync.SyncStatus
import java.time.OffsetDateTime
import java.util.UUID

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey
    val id: Int,
    val uuid: UUID,
    val title: String?,
    val post: String,
    val date: OffsetDateTime,
    val created: OffsetDateTime,
    val updated: OffsetDateTime,
    val syncStatus: SyncStatus,
)
