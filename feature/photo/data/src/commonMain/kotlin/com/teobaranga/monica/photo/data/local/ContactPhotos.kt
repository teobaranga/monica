package com.teobaranga.monica.photo.data.local

import androidx.room3.ColumnInfo
import androidx.room3.Relation

data class ContactPhotos(
    val contactId: Int,
    @ColumnInfo("avatar_url")
    val avatarUrl: String?,
    @Relation(parentColumns = ["contactId"], entityColumns = ["contactId"])
    val photos: List<PhotoEntity>,
)
