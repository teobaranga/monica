package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.activities.data.CreateContactRequest
import com.teobaranga.monica.data.sync.SyncStatus
import javax.inject.Inject

class ContactUpdateSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val editedEntries = contactDao.getBySyncStatus(SyncStatus.EDITED)

        for (entry in editedEntries) {
            val response = contactApi.updateContact(
                id = entry.contactId,
                request = CreateContactRequest(
                    firstName = entry.firstName,
                    lastName = entry.lastName,
                    nickname = entry.nickname,
                    genderId = 1,
                    isBirthdateKnown = false,
                    isDeceased = false,
                    isDeceasedDateKnown = false,
                ),
            )
            when (response) {
                is ApiResponse.Success -> {
                    val entity = response.data.data.toEntity()
                    contactDao.sync(entry.contactId, entity)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
