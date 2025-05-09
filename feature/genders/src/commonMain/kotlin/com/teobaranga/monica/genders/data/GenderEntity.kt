package com.teobaranga.monica.genders.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genders")
data class GenderEntity(
    @PrimaryKey
    val genderId: Int,
    val name: String,
)
