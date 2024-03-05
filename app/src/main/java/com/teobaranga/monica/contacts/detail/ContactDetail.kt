package com.teobaranga.monica.contacts.detail

import androidx.compose.runtime.Immutable
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.detail.ui.ContactInfoSection
import com.teobaranga.monica.ui.avatar.UserAvatar
import java.time.MonthDay
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Immutable
data class ContactDetail(
    val fullName: String,
    val userAvatar: UserAvatar,
    val infoSections: List<ContactInfoSection>,
) {
    sealed interface Birthday {
        data class AgeBased(val age: Int) : Birthday
        data class UnknownYear(val monthDay: MonthDay) : Birthday
        data class Full(val date: ZonedDateTime, val age: Int) : Birthday
    }
}

fun ContactEntity.Birthdate.toBirthday(): ContactDetail.Birthday {
    return when {
        isAgeBased -> {
            val age = ChronoUnit.YEARS.between(date, ZonedDateTime.now())
            ContactDetail.Birthday.AgeBased(age.toInt())
        }

        isYearUnknown -> {
            val monthDay = MonthDay.from(date)
            ContactDetail.Birthday.UnknownYear(monthDay)
        }

        else -> {
            val age = ChronoUnit.YEARS.between(date, ZonedDateTime.now())
            ContactDetail.Birthday.Full(date, age.toInt())
        }
    }
}
