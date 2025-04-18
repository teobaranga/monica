package com.teobaranga.monica.contacts.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import com.teobaranga.monica.datetime.InstantExt.toSystemLocalDateTime
import com.teobaranga.monica.datetime.InstantExt.yearsUntilToday

@Immutable
data class ContactDetail(
    val id: Int,
    val fullName: String,
    val infoSections: List<ContactInfoSection>,
)

fun ContactEntity.Birthdate.toUiBirthday(): Birthday {
    return when {
        isAgeBased -> {
            Birthday.AgeBased(date.yearsUntilToday())
        }

        isYearUnknown -> {
            val localDate = date.toSystemLocalDateTime().date
            Birthday.UnknownYear(MonthDay.from(localDate))
        }

        else -> {
            Birthday.Full(date)
        }
    }
}
