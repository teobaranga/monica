package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.data.sync.SyncStatus
import javax.inject.Inject

@me.tatarka.inject.annotations.Inject
class ContactDeleteSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val deletedContacts = contactDao.getBySyncStatus(SyncStatus.DELETED)

        for (deletedContact in deletedContacts) {
            val contactId = deletedContact.contactId
            when (val response = contactApi.deleteContact(contactId)) {
                is ApiResponse.Success -> {
                    contactDao.delete(listOf(contactId))
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
