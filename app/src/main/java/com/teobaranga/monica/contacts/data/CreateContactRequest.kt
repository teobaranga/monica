package com.teobaranga.monica.activities.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.teobaranga.monica.data.adapter.AlwaysSerializeNulls

@JsonClass(generateAdapter = true)
@AlwaysSerializeNulls
data class CreateContactRequest(
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "nickname")
    val nickname: String?,
    @Json(name = "gender_id")
    val genderId: Int,
    @Json(name = "is_birthdate_known")
    val isBirthdateKnown: Boolean,
    @Json(name = "is_deceased")
    val isDeceased: Boolean,
    @Json(name = "is_deceased_date_known")
    val isDeceasedDateKnown: Boolean,
)
