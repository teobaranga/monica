package com.teobaranga.monica.contacts.ui

import com.teobaranga.monica.contacts.data.ContactEntity
import java.time.LocalDate
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

sealed interface Birthday {

    data class AgeBased(val age: Int) : Birthday

    data class UnknownYear(val monthDay: MonthDay) : Birthday

    data class Full(val date: OffsetDateTime) : Birthday {

        val age: Int
            get() = ChronoUnit.YEARS.between(date, OffsetDateTime.now()).toInt()
    }
}

fun Birthday.toDomainBirthday(): ContactEntity.Birthdate {
    return when (this) {
        is Birthday.AgeBased -> ContactEntity.Birthdate(
            isAgeBased = true,
            isYearUnknown = false,
            date = OffsetDateTime.now().minusYears(age.toLong())
                .withMonth(1)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.HOURS),
        )

        is Birthday.UnknownYear -> ContactEntity.Birthdate(
            isAgeBased = false,
            isYearUnknown = true,
            date = OffsetDateTime.of(
                LocalDate.now().withMonth(monthDay.monthValue).withDayOfMonth(monthDay.dayOfMonth),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC,
            ),
        )

        is Birthday.Full -> ContactEntity.Birthdate(
            isAgeBased = false,
            isYearUnknown = false,
            date = date,
        )
    }
}
