package com.teobaranga.monica.database.adapter

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

object UuidAdapter {

    @TypeConverter
    @JvmStatic
    fun toUuid(value: String?): Uuid? {
        return value?.let {
            Uuid.parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromUuid(uuid: Uuid?): String? {
        return uuid?.toString()
    }
}
