package com.teobaranga.monica.data.user

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "me")
data class MeEntity(
    @PrimaryKey
    val id: Int,
    val firstName: String,
    val contactId: Int?,
)
