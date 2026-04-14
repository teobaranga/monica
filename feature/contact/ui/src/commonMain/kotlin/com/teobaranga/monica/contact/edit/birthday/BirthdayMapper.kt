package com.teobaranga.monica.contact.edit.birthday

import com.teobaranga.monica.contact.Birthday
import com.teobaranga.monica.contact.data.local.ContactEntityBirthdate
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

    fun toUi(birthdate: ContactEntityBirthdate): Birthday {
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

    fun toDomain(birthday: Birthday): ContactEntityBirthdate {
        return when (birthday) {
            is Birthday.AgeBased -> ContactEntityBirthdate(
                isAgeBased = true,
                isYearUnknown = false,
                date = getNowLocalDate()
                    .minus(birthday.age.toLong(), DateTimeUnit.YEAR),
            )

            is Birthday.UnknownYear -> ContactEntityBirthdate(
                isAgeBased = false,
                isYearUnknown = true,
                date = LocalDate(
                    year = getNowLocalDate().year,
                    month = birthday.monthDay.month.number,
                    day = birthday.monthDay.dayOfMonth,
                ),
            )

            is Birthday.Full -> ContactEntityBirthdate(
                isAgeBased = false,
                isYearUnknown = false,
                date = birthday.date,
            )
        }
    }
}
