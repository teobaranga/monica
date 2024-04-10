package com.teobaranga.monica.activities.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.teobaranga.monica.contacts.data.ContactResponse
import com.teobaranga.monica.data.common.MetaResponse

@JsonClass(generateAdapter = true)
data class ContactActivitiesResponse(
    @Json(name = "data")
    val data: List<ContactActivity>,
    @Json(name = "meta")
    val meta: MetaResponse,
) {
    @JsonClass(generateAdapter = true)
    data class ContactActivity(
        @Json(name = "id")
        val id: Int,
        @Json(name = "summary")
        val summary: String,
        @Json(name = "description")
        val description: String?,
        @Json(name = "happened_at")
        val happenedAt: String,
        @Json(name = "attendees")
        val attendees: Attendees,
    ) {
        @JsonClass(generateAdapter = true)
        data class Attendees(
            @Json(name = "contacts")
            val contacts: List<ContactResponse>,
        )
    }
}
