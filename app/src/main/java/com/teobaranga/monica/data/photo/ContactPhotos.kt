package com.teobaranga.monica.data.photo

import androidx.room.Relation

data class ContactPhotos(
    val contactId: Int,
    val avatarUrl: String?,
    @Relation(parentColumn = "contactId", entityColumn = "contactId")
    val photos: List<PhotoEntity>,
)
