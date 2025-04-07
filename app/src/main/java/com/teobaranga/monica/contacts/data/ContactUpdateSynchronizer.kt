package com.teobaranga.monica.contacts.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.core.data.sync.SyncStatus
import me.tatarka.inject.annotations.Inject

@Inject
class ContactUpdateSynchronizer(
    private val contactApi: ContactApi,
    private val contactDao: ContactDao,
    private val contactRequestMapper: ContactRequestMapper,
    private val contactEntityMapper: ContactEntityMapper,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val editedEntries = contactDao.getBySyncStatus(SyncStatus.EDITED)

        for (entry in editedEntries) {
            val request = contactRequestMapper(entry)
            val response = contactApi.updateContact(
                id = entry.contactId,
                request = request,
            )
            when (response) {
                is ApiResponse.Success -> {
                    val entity = contactEntityMapper(response.data.data)
                    contactDao.sync(entry.contactId, entity)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
