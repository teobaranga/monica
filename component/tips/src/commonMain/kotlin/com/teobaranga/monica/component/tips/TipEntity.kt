package com.teobaranga.monica.component.tips

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tips")
data class TipEntity(
    @PrimaryKey
    val id: String,
    val isSeen: Boolean,
)
