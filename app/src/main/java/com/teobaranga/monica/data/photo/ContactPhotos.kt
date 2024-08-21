package com.teobaranga.monica.data.photo

import androidx.room.ColumnInfo
import androidx.room.Relation

data class ContactPhotos(
    val contactId: Int,
    @ColumnInfo("avatar_url")
    val avatarUrl: String?,
    @Relation(parentColumn = "contactId", entityColumn = "contactId")
    val photos: List<PhotoEntity>,
)
