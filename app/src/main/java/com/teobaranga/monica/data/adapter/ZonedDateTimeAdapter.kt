package com.teobaranga.monica.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime

class ZonedDateTimeAdapter {
    @ToJson
    fun toJson(zonedDateTime: ZonedDateTime): String {
        return zonedDateTime.toString()
    }

    @FromJson
    fun fromJson(zonedDateTime: String): ZonedDateTime {
        return ZonedDateTime.parse(zonedDateTime)
    }
}
