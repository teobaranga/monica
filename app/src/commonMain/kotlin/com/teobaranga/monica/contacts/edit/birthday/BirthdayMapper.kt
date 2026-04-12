package com.teobaranga.monica.contacts.edit.birthday

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.yearsUntil
import me.tatarka.inject.annotations.Inject

@Inject
class BirthdayMapper(
    private val getNowLocalDate: () -> LocalDate,
) {

    fun toUi(birthdate: ContactEntity.Birthdate): Birthday {
        return when {
            birthdate.isAgeBased -> {
                Birthday.AgeBased(birthdate.date.yearsUntil(getNowLocalDate()))
            }

            birthdate.isYearUnknown -> {
                val localDate = birthdate.date
                Birthday.UnknownYear(MonthDay.from(localDate))
            }

            else -> {
                Birthday.Full(birthdate.date)
            }
        }
    }

    fun toDomain(birthday: Birthday): ContactEntity.Birthdate {
        return when (birthday) {
            is Birthday.AgeBased -> ContactEntity.Birthdate(
                isAgeBased = true,
                isYearUnknown = false,
                date = getNowLocalDate()
                    .minus(birthday.age.toLong(), DateTimeUnit.YEAR),
            )

            is Birthday.UnknownYear -> ContactEntity.Birthdate(
                isAgeBased = false,
                isYearUnknown = true,
                date = LocalDate(
                    year = getNowLocalDate().year,
                    month = birthday.monthDay.month.number,
                    day = birthday.monthDay.dayOfMonth,
                ),
            )

            is Birthday.Full -> ContactEntity.Birthdate(
                isAgeBased = false,
                isYearUnknown = false,
                date = birthday.date,
            )
        }
    }
}
