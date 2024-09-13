package com.teobaranga.monica.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlin.uuid.Uuid

class UuidAdapter {
    @ToJson
    fun toJson(uuid: Uuid): String {
        return uuid.toString()
    }

    @FromJson
    fun fromJson(uuid: String): Uuid {
        return Uuid.parse(uuid)
    }
}
