package com.teobaranga.monica.contacts.data

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import me.tatarka.inject.annotations.Inject

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
        return birthdate?.date?.toLocalDateTime(TimeZone.currentSystemDefault())?.dayOfMonth
    }

    private fun ContactEntity.getBirthdateMonth(): Int? {
        return birthdate?.date?.toLocalDateTime(TimeZone.currentSystemDefault())?.monthNumber
    }

    private fun ContactEntity.getBirthdateYear(): Int? {
        return birthdate?.run {
            date.toLocalDateTime(TimeZone.currentSystemDefault()).year.takeIf { !isYearUnknown }
        }
    }

    private fun ContactEntity.getBirthdateAge(): Int? {
        return birthdate?.date?.run {
            yearsUntil(Clock.System.now(), TimeZone.currentSystemDefault())
        }
    }
}
