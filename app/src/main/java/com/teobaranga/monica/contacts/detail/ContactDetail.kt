package com.teobaranga.monica.contacts.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection
import com.teobaranga.monica.contacts.ui.Birthday
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Immutable
data class ContactDetail(
    val id: Int,
    val fullName: String,
    val infoSections: List<ContactInfoSection>,
)

fun ContactEntity.Birthdate.toUiBirthday(): Birthday {
    return when {
        isAgeBased -> {
            val age = ChronoUnit.YEARS.between(date, OffsetDateTime.now()).toInt()
            Birthday.AgeBased(age)
        }

        isYearUnknown -> {
            val monthDay = MonthDay.from(date)
            Birthday.UnknownYear(monthDay)
        }

        else -> {
            Birthday.Full(date)
        }
    }
}
