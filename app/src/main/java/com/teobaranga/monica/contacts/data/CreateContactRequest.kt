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
    val genderId: Int?,
    @Json(name = "birthdate_day")
    val birthdateDay: Int?,
    @Json(name = "birthdate_month")
    val birthdateMonth: Int?,
    @Json(name = "birthdate_year")
    val birthdateYear: Int?,
    @Json(name = "birthdate_is_age_based")
    val birthdateIsAgeBased: Boolean,
    @Json(name = "is_birthdate_known")
    val isBirthdateKnown: Boolean,
    @Json(name = "birthdate_age")
    val birthdateAge: Int?,
    @Json(name = "is_deceased")
    val isDeceased: Boolean,
    @Json(name = "is_deceased_date_known")
    val isDeceasedDateKnown: Boolean,
)
