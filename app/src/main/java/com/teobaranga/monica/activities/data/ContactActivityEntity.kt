package com.teobaranga.monica.activities.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "contact_activity")
data class ContactActivityEntity(
    @PrimaryKey
    val activityId: Int,
    val title: String,
    val description: String?,
    val date: LocalDate,
)
