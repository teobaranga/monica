package com.teobaranga.monica.photo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: Int,
    val contactId: Int,
    val fileName: String,
    val data: String?,
)
