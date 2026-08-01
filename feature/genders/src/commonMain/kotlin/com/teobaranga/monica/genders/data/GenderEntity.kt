package com.teobaranga.monica.genders.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "genders")
data class GenderEntity(
    @PrimaryKey
    val genderId: Int,
    val name: String,
)
