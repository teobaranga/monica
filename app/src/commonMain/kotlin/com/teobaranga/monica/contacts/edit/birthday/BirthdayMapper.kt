package com.teobaranga.monica.contacts.edit.birthday

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.format
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearsUntil
import me.tatarka.inject.annotations.Inject

@Inject
class BirthdayMapper(
    private val clock: Clock,
    private val timeZone: TimeZone,
    private val getNowLocalDate: () -> LocalDate,
) {

    fun toUi(birthdate: ContactEntity.Birthdate): Birthday {
        return when {
            birthdate.isAgeBased -> {
                Birthday.AgeBased(birthdate.date.yearsUntil(clock.now(), timeZone))
            }

            birthdate.isYearUnknown -> {
                val localDate = birthdate.date.toLocalDateTime(timeZone).date
                Birthday.UnknownYear(MonthDay.from(localDate))
            }

            else -> {
                Birthday.Full(birthdate.date.toLocalDateTime(timeZone).date)
            }
        }
    }

    fun toDomain(birthday: Birthday): ContactEntity.Birthdate {
        return when (birthday) {
            is Birthday.AgeBased -> ContactEntity.Birthdate(
                isAgeBased = true,
                isYearUnknown = false,
                date = getNowLocalDate()
                    .minus(birthday.age.toLong(), DateTimeUnit.YEAR)
                    .atStartOfDayIn(timeZone),
            )

            is Birthday.UnknownYear -> ContactEntity.Birthdate(
                isAgeBased = false,
                isYearUnknown = true,
                date = run {
                    val format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET.format {
                        month = birthday.monthDay.month
                        dayOfMonth = birthday.monthDay.dayOfMonth
                        // When the year is unknown, the provided year can be anything
                        year = clock.todayIn(timeZone).year
                        hour = 0
                        minute = 0
                        second = 0
                        nanosecond = 0
                        offsetHours = 0
                        offsetMinutesOfHour = 0
                        offsetSecondsOfMinute = 0
                    }
                    Instant.parse(format)
                },
            )

            is Birthday.Full -> ContactEntity.Birthdate(
                isAgeBased = false,
                isYearUnknown = false,
                date = birthday.date.atStartOfDayIn(timeZone),
            )
        }
    }
}
