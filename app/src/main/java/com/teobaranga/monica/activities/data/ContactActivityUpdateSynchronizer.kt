package com.teobaranga.monica.activities.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.data.sync.SyncStatus
import javax.inject.Inject

class ContactActivityUpdateSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactActivitiesDao: ContactActivitiesDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val editedEntries = contactActivitiesDao.getActivitiesByStatus(SyncStatus.EDITED)

        for (entry in editedEntries) {
            val response = contactApi.updateActivity(
                id = entry.activity.activityId,
                request = CreateActivityRequest(
                    activityTypeId = null,
                    summary = entry.activity.title,
                    description = entry.activity.description,
                    date = entry.activity.date,
                    contacts = entry.participants.map { it.contactId },
                    emotions = null,
                ),
            )
            when (response) {
                is ApiResponse.Success -> {
                    contactActivitiesDao.setSyncStatus(entry.activity.activityId, SyncStatus.UP_TO_DATE)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
