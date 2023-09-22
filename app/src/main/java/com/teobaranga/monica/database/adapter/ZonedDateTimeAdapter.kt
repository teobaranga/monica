package com.teobaranga.monica.database.adapter

import androidx.room.TypeConverter
import java.time.ZonedDateTime

object ZonedDateTimeAdapter {

    @TypeConverter
    @JvmStatic
    fun toZonedDateTime(value: String?): ZonedDateTime? {
        return value?.let {
            ZonedDateTime.parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromZonedDateTime(date: ZonedDateTime?): String? {
        return date?.toString()
    }
}
