package com.teobaranga.monica.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

class LocalDateAdapter {
    @ToJson
    fun toJson(localDate: LocalDate): String {
        return localDate.toString()
    }

    @FromJson
    fun fromJson(localDate: String): LocalDate {
        return LocalDate.parse(localDate)
    }
}
