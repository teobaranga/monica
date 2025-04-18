package com.teobaranga.monica.database.adapter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.alternativeParsing

object InstantAdapter {

    @TypeConverter
    @JvmStatic
    fun to(value: String?): Instant? {
        return value?.let {
            Instant.parse(value, DateTimeComponents.Format {
                dateTime(LocalDateTime.Formats.ISO)
                alternativeParsing({
                    offsetHours()
                }) {
                    offset(UtcOffset.Formats.ISO)
                }
            })
        }
    }

    @TypeConverter
    @JvmStatic
    fun from(date: Instant?): String? {
        return date?.toString()
    }
}
