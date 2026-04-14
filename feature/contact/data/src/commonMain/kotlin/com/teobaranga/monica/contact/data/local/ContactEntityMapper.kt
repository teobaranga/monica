package com.teobaranga.monica.contact.data.local

import com.teobaranga.monica.contact.data.remote.ContactResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.genders.data.GenderRepository
import me.tatarka.inject.annotations.Inject

@Inject
class ContactEntityMapper(
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
                avatar = ContactEntityAvatar(
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

    private fun ContactResponse.Information.Dates.Birthdate.toBirthday(): ContactEntityBirthdate? {
        return date?.let { date ->
            ContactEntityBirthdate(
                isAgeBased = isAgeBased == true,
                isYearUnknown = isYearUnknown == true,
                date = date,
            )
        }
    }
}
