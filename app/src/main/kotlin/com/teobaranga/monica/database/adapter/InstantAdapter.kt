package com.teobaranga.monica.database.adapter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.format.DateTimeComponents

object InstantAdapter {

    @TypeConverter
    @JvmStatic
    fun to(value: String?): Instant? {
        return value?.let {
            Instant.parse(value, DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)
        }
    }

    @TypeConverter
    @JvmStatic
    fun from(date: Instant?): String? {
        return date?.toString()
    }
}
