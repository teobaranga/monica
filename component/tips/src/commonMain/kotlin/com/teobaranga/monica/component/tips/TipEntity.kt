package com.teobaranga.monica.component.tips

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tips")
data class TipEntity(
    @PrimaryKey
    val id: String,
    val isSeen: Boolean,
)
