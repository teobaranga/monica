package com.teobaranga.monica.contacts.data

import me.tatarka.inject.annotations.Inject
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Inject
class ContactRequestMapper {

    operator fun invoke(entity: ContactEntity): CreateContactRequest {
        return CreateContactRequest(
            firstName = entity.firstName,
            lastName = entity.lastName,
            nickname = entity.nickname,
            genderId = entity.genderId,
            birthdateDay = entity.getBirthdateDay(),
            birthdateMonth = entity.getBirthdateMonth(),
            birthdateYear = entity.getBirthdateYear(),
            isBirthdateKnown = entity.birthdate != null,
            birthdateIsAgeBased = entity.birthdate?.isAgeBased ?: false,
            birthdateAge = entity.getBirthdateAge(),
            isDeceased = false,
            isDeceasedDateKnown = false,
        )
    }

    private fun ContactEntity.getBirthdateDay(): Int? {
        return birthdate?.date?.dayOfMonth
    }

    private fun ContactEntity.getBirthdateMonth(): Int? {
        return birthdate?.date?.monthValue
    }

    private fun ContactEntity.getBirthdateYear(): Int? {
        return birthdate?.run {
            date.year.takeIf { !isYearUnknown }
        }
    }

    private fun ContactEntity.getBirthdateAge(): Int? {
        return birthdate?.date?.run {
            ChronoUnit.YEARS.between(this, OffsetDateTime.now()).toInt()
        }
    }
}
