package com.teobaranga.monica.contacts.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateContactRequest(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("gender_id")
    val genderId: Int?,
    @SerialName("birthdate_day")
    val birthdateDay: Int?,
    @SerialName("birthdate_month")
    val birthdateMonth: Int?,
    @SerialName("birthdate_year")
    val birthdateYear: Int?,
    @SerialName("birthdate_is_age_based")
    val birthdateIsAgeBased: Boolean,
    @SerialName("is_birthdate_known")
    val isBirthdateKnown: Boolean,
    @SerialName("birthdate_age")
    val birthdateAge: Int?,
    @SerialName("is_deceased")
    val isDeceased: Boolean,
    @SerialName("is_deceased_date_known")
    val isDeceasedDateKnown: Boolean,
)
