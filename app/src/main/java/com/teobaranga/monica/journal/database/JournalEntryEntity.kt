package com.teobaranga.monica.journal.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.data.sync.SyncStatus
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey
    val id: Int,
    val uuid: Uuid,
    val title: String?,
    val post: String,
    val date: LocalDate,
    val created: OffsetDateTime,
    val updated: OffsetDateTime,
    val syncStatus: SyncStatus,
)
