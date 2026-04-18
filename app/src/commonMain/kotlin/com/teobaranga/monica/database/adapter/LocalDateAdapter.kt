package com.teobaranga.monica.database.adapter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.jvm.JvmStatic
import kotlin.time.Instant

object LocalDateAdapter {

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            try {
                LocalDate.parse(value)
            } catch (_: Exception) {
                Instant.parse(value).toLocalDateTime(TimeZone.UTC).date
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }
}
