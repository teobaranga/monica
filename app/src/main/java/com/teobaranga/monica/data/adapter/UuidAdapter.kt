package com.teobaranga.monica.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.UUID

class UuidAdapter {
    @ToJson
    fun toJson(uuid: UUID): String {
        return uuid.toString()
    }

    @FromJson
    fun fromJson(uuid: String): UUID {
        return UUID.fromString(uuid)
    }
}
