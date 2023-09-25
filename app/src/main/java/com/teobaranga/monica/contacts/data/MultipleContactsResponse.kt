package com.teobaranga.monica.contacts.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.teobaranga.monica.data.common.MetaResponse

@JsonClass(generateAdapter = true)
data class MultipleContactsResponse(
    @Json(name = "data")
    val data: List<ContactResponse>,
    @Json(name = "meta")
    val meta: MetaResponse,
)
