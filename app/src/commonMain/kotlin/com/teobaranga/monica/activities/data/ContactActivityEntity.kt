package com.teobaranga.monica.activities.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teobaranga.monica.core.data.sync.SyncStatus
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(tableName = "contact_activity")
data class ContactActivityEntity(
    /**
     * Activity ID defined by the server.
     */
    @PrimaryKey
    val activityId: Int,
    /**
     * Local identifier.
     */
    val uuid: Uuid,
    val title: String,
    val description: String?,
    val date: LocalDate,
    val created: Instant,
    val updated: Instant,
    val syncStatus: SyncStatus,
)
