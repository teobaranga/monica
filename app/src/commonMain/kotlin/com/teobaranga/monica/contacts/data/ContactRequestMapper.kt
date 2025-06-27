package com.teobaranga.monica.contacts.data

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import me.tatarka.inject.annotations.Inject
import kotlin.time.Clock

@Inject
class ContactRequestMapper(
    private val clock: Clock,
    private val timeZone: TimeZone,
) {

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
        return birthdate?.run {
            if (isAgeBased) {
                null
            } else {
                date.toLocalDateTime(timeZone).day
            }
        }
    }

    private fun ContactEntity.getBirthdateMonth(): Int? {
        return birthdate?.run {
            if (isAgeBased) {
                null
            } else {
                date.toLocalDateTime(timeZone).month.number
            }
        }
    }

    private fun ContactEntity.getBirthdateYear(): Int? {
        return birthdate?.run {
            date.toLocalDateTime(timeZone).year.takeIf { !isYearUnknown }
        }
    }

    private fun ContactEntity.getBirthdateAge(): Int? {
        return birthdate?.run {
            if (isAgeBased) {
                date.yearsUntil(clock.now(), timeZone)
            } else {
                null
            }
        }
    }
}
