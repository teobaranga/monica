package com.teobaranga.monica.activities.data

import com.teobaranga.monica.contacts.data.ContactResponse
import com.teobaranga.monica.data.adapter.LocalDateAsString
import com.teobaranga.monica.data.adapter.OffsetDateTimeAsString
import com.teobaranga.monica.data.common.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactActivitiesResponse(
    @SerialName("data")
    val data: List<ContactActivity>,
    @SerialName("meta")
    val meta: MetaResponse,
) {
    @Serializable
    data class ContactActivity(
        @SerialName("id")
        val id: Int,
        @SerialName("uuid")
        val uuid: String,
        @SerialName("summary")
        val summary: String,
        @SerialName("description")
        val description: String?,
        @SerialName("happened_at")
        val happenedAt: LocalDateAsString,
        @SerialName("attendees")
        val attendees: Attendees,
        @SerialName("created_at")
        val created: OffsetDateTimeAsString,
        @SerialName("updated_at")
        val updated: OffsetDateTimeAsString,
    ) {
        @Serializable
        data class Attendees(
            @SerialName("contacts")
            val contacts: List<ContactResponse>,
        )
    }
}
