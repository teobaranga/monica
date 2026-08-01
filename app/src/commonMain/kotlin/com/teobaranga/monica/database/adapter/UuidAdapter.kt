package com.teobaranga.monica.database.adapter

import androidx.room3.ColumnTypeConverter
import kotlin.jvm.JvmStatic
import kotlin.uuid.Uuid

object UuidAdapter {

    @ColumnTypeConverter
    @JvmStatic
    fun toUuid(value: String?): Uuid? {
        return value?.let {
            Uuid.parse(value)
        }
    }

    @ColumnTypeConverter
    @JvmStatic
    fun fromUuid(uuid: Uuid?): String? {
        return uuid?.toString()
    }
}
