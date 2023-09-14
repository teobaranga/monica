package com.teobaranga.monica.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "me")
data class MeEntity(
    @PrimaryKey
    val id: Int,
    val firstName: String,
    val contactId: Int?,
)
