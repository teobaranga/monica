package com.teobaranga.monica.data.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: Int,
    val firstName: String,
    val lastName: String?,
    val initials: String,
    val avatarUrl: String?,
    val avatarColor: String,
)
