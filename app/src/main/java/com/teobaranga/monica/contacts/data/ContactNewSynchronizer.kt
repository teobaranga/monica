package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.data.sync.SyncStatus
import javax.inject.Inject

class ContactNewSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
    private val contactRequestMapper: ContactRequestMapper,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val newContacts = contactDao.getBySyncStatus(SyncStatus.NEW)

        for (entry in newContacts) {
            val request = contactRequestMapper(entry)
            when (val response = contactApi.createContact(request)) {
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
