package com.teobaranga.monica.activities.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "contact_activity_cross_refs",
    primaryKeys = ["contactId", "activityId"],
)
data class ContactActivityCrossRef(
    val contactId: Int,
    @ColumnInfo(index = true)
    val activityId: Int,
)
