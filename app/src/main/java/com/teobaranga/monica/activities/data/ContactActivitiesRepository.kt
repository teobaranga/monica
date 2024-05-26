package com.teobaranga.monica.activities.data

import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ContactActivitiesRepository @Inject constructor(
    dispatcher: Dispatcher,
    private val contactActivitiesDao: ContactActivitiesDao,
    private val contactActivityNewSynchronizer: ContactActivityNewSynchronizer,
    private val contactActivityDeletedSynchronizer: ContactActivityDeletedSynchronizer,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    fun getActivities(contactId: Int): Flow<List<ContactActivityWithParticipants>> {
        return contactActivitiesDao.getContactActivities(contactId)
    }

    fun getActivity(activityId: Int): Flow<ContactActivityEntity> {
        return contactActivitiesDao.getActivity(activityId)
    }

    suspend fun insertActivity(title: String, description: String?, date: LocalDate, participants: List<Int>) {
        /**
         * Add new entry to Room with id = max(id) + 1
         * Create new entry using API
         * Insert response into Room, should ideally have a similar ID but keep a map of local to remote ID
         */
        val localId = contactActivitiesDao.getMaxId() + 1
        val createdDate = OffsetDateTime.now()
        val entity = ContactActivityEntity(
            activityId = localId,
            title = title,
            description = description,
            date = date,
            created = createdDate,
            updated = createdDate,
            syncStatus = SyncStatus.NEW,
        )
        val crossRefs = participants
            .map {
                ContactActivityCrossRef(
                    contactId = it,
                    activityId = localId,
                )
            }
        contactActivitiesDao.upsert(
            activities = listOf(entity),
            crossRefs = crossRefs,
        )
        scope.launch {
            contactActivityNewSynchronizer.sync()
        }
    }

    suspend fun deleteActivity(activityId: Int) {
        contactActivitiesDao.setSyncStatus(activityId, SyncStatus.DELETED)
        scope.launch {
            contactActivityDeletedSynchronizer.sync()
        }
    }
}
