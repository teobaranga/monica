package com.teobaranga.monica.user.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "me")
data class MeEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val contactId: Int?,
)
