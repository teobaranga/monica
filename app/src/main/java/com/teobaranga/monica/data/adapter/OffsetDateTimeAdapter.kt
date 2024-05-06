package com.teobaranga.monica.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime

class OffsetDateTimeAdapter {
    @ToJson
    fun toJson(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.toString()
    }

    @FromJson
    fun fromJson(offsetDateTime: String): OffsetDateTime {
        return OffsetDateTime.parse(offsetDateTime)
    }
}
