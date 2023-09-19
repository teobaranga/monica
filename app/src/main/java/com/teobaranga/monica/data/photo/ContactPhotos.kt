package com.teobaranga.monica.data.photo

import androidx.room.Relation

data class ContactPhotos(
    val id: Int,
    val avatarUrl: String?,
    @Relation(parentColumn = "id", entityColumn = "contactId")
    val photos: List<PhotoEntity>,
)
