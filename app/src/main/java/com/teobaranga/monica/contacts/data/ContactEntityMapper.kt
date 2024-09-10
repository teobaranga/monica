package com.teobaranga.monica.contacts.data

import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.genders.data.GenderRepository
import javax.inject.Inject

class ContactEntityMapper @Inject constructor(
    private val genderRepository: GenderRepository,
) {

    suspend operator fun invoke(data: ContactResponse): ContactEntity {
        val genderId = data.gender?.let { genderRepository.getByName(it) }?.genderId
        return with(data) {
            ContactEntity(
                contactId = id,
                firstName = firstName,
                lastName = lastName,
                nickname = nickname,
                completeName = completeName,
                initials = initials,
                avatar = ContactEntity.Avatar(
                    url = info.avatar.url,
                    color = info.avatar.color,
                ),
                birthdate = info.dates?.birthdate?.toBirthday(),
                genderId = genderId,
                updated = updated,
                syncStatus = SyncStatus.UP_TO_DATE,
            )
        }
    }

    private fun ContactResponse.Information.Dates.Birthdate.toBirthday(): ContactEntity.Birthdate? {
        return date?.let { date ->
            ContactEntity.Birthdate(
                isAgeBased = isAgeBased == true,
                isYearUnknown = isYearUnknown == true,
                date = date,
            )
        }
    }
}
