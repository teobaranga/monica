package com.teobaranga.monica.activities.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.core.data.sync.SyncStatus
import me.tatarka.inject.annotations.Inject

@Inject
class ContactActivityNewSynchronizer(
    private val contactApi: ContactApi,
    private val contactActivitiesDao: ContactActivitiesDao,
) {

    suspend fun sync() {
        // TODO check for network before syncing

        val newEntries = contactActivitiesDao.getActivitiesByStatus(SyncStatus.NEW)

        for (newEntry in newEntries) {
            val response = contactApi.createActivity(
                CreateActivityRequest(
                    activityTypeId = null,
                    summary = newEntry.activity.title,
                    description = newEntry.activity.description,
                    date = newEntry.activity.date,
                    contacts = newEntry.participants.map { it.contactId },
                    emotions = null,
                ),
            )
            when (response) {
                is ApiResponse.Success -> {
                    val entity = response.data.data.toEntity(
                        getUuid = { newEntry.activity.uuid },
                    )
                    val crossRefs = response.data.data.attendees.contacts
                        .distinctBy { contact ->
                            contact.id
                        }
                        .map { contact ->
                            ContactActivityCrossRef(
                                contactId = contact.id,
                                activityId = entity.activityId,
                            )
                        }

                    contactActivitiesDao.sync(newEntry.activity.activityId, entity, crossRefs)
                }

                else -> {
                    println("ERROR $response")
                }
            }
        }
    }
}
