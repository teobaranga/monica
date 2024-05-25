package com.teobaranga.monica.activities.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.data.sync.SyncStatus
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity(tableName = "contact_activity")
data class ContactActivityEntity(
    @PrimaryKey
    val activityId: Int,
    val title: String,
    val description: String?,
    val date: LocalDate,
    val created: OffsetDateTime,
    val updated: OffsetDateTime,
    val syncStatus: SyncStatus,
)
