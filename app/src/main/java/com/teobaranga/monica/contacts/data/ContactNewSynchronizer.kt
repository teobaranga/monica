package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.activities.data.CreateContactRequest
import com.teobaranga.monica.data.sync.SyncStatus
import javax.inject.Inject

class ContactNewSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val newContacts = contactDao.getBySyncStatus(SyncStatus.NEW)

        for (newContact in newContacts) {
            val response = contactApi.createContact(
                CreateContactRequest(
                    firstName = newContact.firstName,
                    lastName = newContact.lastName,
                    nickname = newContact.nickname,
                    genderId = 1,
                    isBirthdateKnown = false,
                    isDeceased = false,
                    isDeceasedDateKnown = false,
                ),
            )
            when (response) {
                is ApiResponse.Success -> {
                    val entity = response.data.data.toEntity()
                    contactDao.sync(newContact.contactId, entity)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
