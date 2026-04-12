package com.teobaranga.monica.contacts.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.datetime.yearsUntil
import me.tatarka.inject.annotations.Inject

@Inject
class ContactRequestMapper(
    private val nowDate: () -> LocalDate,
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
                date.day
            }
        }
    }

    private fun ContactEntity.getBirthdateMonth(): Int? {
        return birthdate?.run {
            if (isAgeBased) {
                null
            } else {
                date.month.number
            }
        }
    }

    private fun ContactEntity.getBirthdateYear(): Int? {
        return birthdate?.run {
            date.year.takeIf { !isYearUnknown }
        }
    }

    private fun ContactEntity.getBirthdateAge(): Int? {
        return birthdate?.run {
            if (isAgeBased) {
                date.yearsUntil(nowDate())
            } else {
                null
            }
        }
    }
}
