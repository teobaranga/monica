package com.teobaranga.monica.contacts.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil

@Immutable
data class ContactDetail(
    val id: Int,
    val fullName: String,
    val infoSections: List<ContactInfoSection>,
)

fun ContactEntity.Birthdate.toUiBirthday(): Birthday {
    return when {
        isAgeBased -> {
            val age = date.yearsUntil(Clock.System.now(), TimeZone.currentSystemDefault())
            Birthday.AgeBased(age)
        }

        isYearUnknown -> {
            val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
            Birthday.UnknownYear(MonthDay.from(localDate))
        }

        else -> {
            Birthday.Full(date)
        }
    }
}
