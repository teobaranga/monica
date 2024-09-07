package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.activities.data.CreateContactRequest
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ContactRequestMapper @Inject constructor() {

    operator fun invoke(entity: ContactEntity): CreateContactRequest {
        return CreateContactRequest(
            firstName = entity.firstName,
            lastName = entity.lastName,
            nickname = entity.nickname,
            genderId = 1,
            birthdateDay = null,
            birthdateMonth = null,
            birthdateYear = null,
            isBirthdateKnown = entity.birthdate != null,
            birthdateIsAgeBased = entity.birthdate?.isAgeBased ?: false,
            birthdateAge = entity.birthdate?.run {
                ChronoUnit.YEARS.between(date, OffsetDateTime.now()).toInt()
            },
            isDeceased = false,
            isDeceasedDateKnown = false,
        )
    }
}
