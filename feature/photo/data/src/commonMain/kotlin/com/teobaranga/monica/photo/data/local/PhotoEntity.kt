package com.teobaranga.monica.photo.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: Int,
    val contactId: Int,
    val fileName: String,
    val data: String?,
)
