package com.teobaranga.monica.contacts.ui

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.format.format
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil

private val monthDayFormat = DateTimeComponents.Format {
    monthNumber()
    char('-')
    dayOfMonth()
}

sealed interface Birthday {

    data class AgeBased(val age: Int) : Birthday

    data class UnknownYear(val monthDay: MonthDay) : Birthday

    data class Full(val date: Instant) : Birthday {

        val age: Int
            get() = date.yearsUntil(Clock.System.now(), TimeZone.currentSystemDefault())
    }
}

fun Birthday.toDomainBirthday(): ContactEntity.Birthdate {
    return when (this) {
        is Birthday.AgeBased -> ContactEntity.Birthdate(
            isAgeBased = true,
            isYearUnknown = false,
            date = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .minus(age.toLong(), DateTimeUnit.YEAR)
                .atStartOfDayIn(TimeZone.currentSystemDefault()),
        )

        is Birthday.UnknownYear -> ContactEntity.Birthdate(
            isAgeBased = false,
            isYearUnknown = true,
            date = run {
                val format = monthDayFormat.format {
                    month = monthDay.month
                    dayOfMonth = monthDay.dayOfMonth
                }
                Instant.parse(format)
            },
        )

        is Birthday.Full -> ContactEntity.Birthdate(
            isAgeBased = false,
            isYearUnknown = false,
            date = date,
        )
    }
}
