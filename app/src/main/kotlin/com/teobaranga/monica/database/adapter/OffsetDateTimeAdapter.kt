package com.teobaranga.monica.database.adapter

import androidx.room.TypeConverter
import java.time.OffsetDateTime

object OffsetDateTimeAdapter {

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            OffsetDateTime.parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.toString()
    }
}
