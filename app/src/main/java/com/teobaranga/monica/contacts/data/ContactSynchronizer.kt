package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.data.sync.Synchronizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
) : Synchronizer {

    override val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    override suspend fun sync() {
        syncState.value = Synchronizer.State.REFRESHING

        // Keep track of removed contacts, start with the full database first
        val removedIds = contactDao.getContactIds().first().toMutableSet()

        var nextPage: Int? = 1
        while (nextPage != null) {
            val contactsResponse = contactApi.getContacts(page = nextPage, sort = "-updated_at")
                .onFailure {
                    Timber.w("Error while loading contacts: %s", this)
                }
                .getOrElse {
                    syncState.value = Synchronizer.State.IDLE
                    return
                }
            val contactEntities = contactsResponse.data
                .map {
                    it.toEntity()
                }

            contactDao.upsertContacts(contactEntities)

            contactsResponse.meta.run {
                nextPage = if (currentPage != lastPage) {
                    currentPage + 1
                } else {
                    null
                }
            }

            // Reduce the list of entries to be removed based on the entries previously inserted
            removedIds -= contactEntities.map { it.contactId }.toSet()
        }

        contactDao.delete(removedIds.toList())

        syncState.value = Synchronizer.State.IDLE
    }
}

internal fun ContactResponse.toEntity(): ContactEntity {
    return ContactEntity(
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
        updated = updated,
        syncStatus = SyncStatus.UP_TO_DATE,
    )
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
