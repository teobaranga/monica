package com.teobaranga.monica.journal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.core.data.sync.SyncStatus
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey
    val id: Int,
    val uuid: Uuid,
    val title: String?,
    val post: String,
    val date: LocalDate,
    val created: Instant,
    val updated: Instant,
    val syncStatus: SyncStatus,
)
