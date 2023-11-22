package com.teobaranga.monica.contacts.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.teobaranga.monica.data.common.MetaResponse

@JsonClass(generateAdapter = true)
data class ContactPhotosResponse(
    @Json(name = "data")
    val data: List<Photo>,
    @Json(name = "meta")
    val meta: MetaResponse,
) {
    @JsonClass(generateAdapter = true)
    data class Photo(
        @Json(name = "id")
        val id: Int,
        @Json(name = "new_filename")
        val fileName: String,
        @Json(name = "dataUrl")
        val data: String,
        @Json(name = "contact")
        val contact: ContactResponse,
    )
}
