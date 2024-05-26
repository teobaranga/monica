package com.teobaranga.monica.activities.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.data.sync.SyncStatus
import javax.inject.Inject

class ContactActivityDeletedSynchronizer @Inject constructor(
    private val contactApi: ContactApi,
    private val contactActivitiesDao: ContactActivitiesDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val deletedEntries = contactActivitiesDao.getActivitiesByStatus(SyncStatus.DELETED)

        for (entry in deletedEntries) {
            val activityId = entry.activity.activityId
            when (val response = contactApi.deleteActivity(activityId)) {
                is ApiResponse.Success -> {
                    contactActivitiesDao.delete(listOf(activityId))
                }
                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
