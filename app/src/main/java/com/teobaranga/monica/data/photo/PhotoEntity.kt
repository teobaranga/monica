package com.teobaranga.monica.data.photo

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
