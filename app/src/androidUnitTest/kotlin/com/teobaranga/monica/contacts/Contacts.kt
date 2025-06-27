package com.teobaranga.monica.contacts

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactResponse
import com.teobaranga.monica.contacts.data.CreateContactRequest
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.genders.data.genderMale
import kotlin.time.Instant

const val TEST_CONTACT_ID = 123

fun newContactEntity(
    contactId: Int,
    firstName: String,
    avatar: ContactEntity.Avatar = ContactEntity.Avatar(
        url = null,
        color = "#000000",
    ),
    lastName: String? = null,
    nickname: String? = null,
    birthdate: ContactEntity.Birthdate? = null,
    genderId: Int? = null,
    updated: Instant? = null,
    syncStatus: SyncStatus = SyncStatus.NEW,
): ContactEntity {
    return ContactEntity(
        contactId = contactId,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        completeName = buildString {
            append(firstName)
            if (lastName != null) {
                append(" ")
                append(lastName)
            }
            if (nickname != null) {
                append(" (")
                append(nickname)
                append(")")
            }
        },
        initials = buildString {
            append(firstName.take(1))
            if (lastName != null) {
                append(lastName.take(1))
            }
        },
        birthdate = birthdate,
        genderId = genderId,
        updated = updated,
        avatar = avatar,
        syncStatus = syncStatus,
    )
}

val validContact = newContactEntity(
    contactId = TEST_CONTACT_ID,
    firstName = "John",
    lastName = "Doe",
    nickname = "Johnny",
    birthdate = null,
    genderId = genderMale.genderId,
)

fun ContactEntity.toCreateRequest(
    birthdayDay: Int? = null,
    birthdayMonth: Int? = null,
    birthdayYear: Int? = null,
    birthdateAge: Int? = null,
    birthdateIsAgeBased: Boolean = false,
    isBirthdateKnown: Boolean = false,
): CreateContactRequest {
    return CreateContactRequest(
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        genderId = genderId,
        birthdateDay = birthdayDay,
        birthdateMonth = birthdayMonth,
        birthdateYear = birthdayYear,
        birthdateAge = birthdateAge,
        birthdateIsAgeBased = birthdateIsAgeBased,
        isBirthdateKnown = isBirthdateKnown,
        isDeceased = false,
        isDeceasedDateKnown = false,
    )
}

fun ContactEntity.toResponse(
    gender: String? = null,
    birthday: Instant? = birthdate?.date,
    birthdateIsAgeBased: Boolean = false,
    birthdateIsYearUnknown: Boolean = false,
): ContactResponse {
    return ContactResponse(
        id = contactId,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        completeName = completeName,
        initials = initials,
        gender = gender,
        info = ContactResponse.Information(
            avatar = ContactResponse.Information.Avatar(
                url = avatar.url,
                color = avatar.color,
            ),
            dates = ContactResponse.Information.Dates(
                birthdate = ContactResponse.Information.Dates.Birthdate(
                    isAgeBased = birthdateIsAgeBased,
                    isYearUnknown = birthdateIsYearUnknown,
                    date = birthday,
                ),
            )
        ),
    )
}
