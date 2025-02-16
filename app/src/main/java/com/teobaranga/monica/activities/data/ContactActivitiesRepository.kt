package com.teobaranga.monica.activities.data

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.sync.SyncStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

@Inject
@SingleIn(AppScope::class)
internal class ContactActivitiesRepository(
    dispatcher: Dispatcher,
    private val contactActivitiesDao: ContactActivitiesDao,
    private val contactActivityNewSynchronizer: ContactActivityNewSynchronizer,
    private val contactActivityUpdateSynchronizer: ContactActivityUpdateSynchronizer,
    private val contactActivityDeletedSynchronizer: ContactActivityDeletedSynchronizer,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    fun getActivities(contactId: Int): Flow<List<ContactActivityWithParticipants>> {
        return contactActivitiesDao.getContactActivities(contactId)
    }

    fun getActivity(activityId: Int): Flow<ContactActivityWithParticipants> {
        return contactActivitiesDao.getActivity(activityId)
    }

    suspend fun upsertActivity(
        activityId: Int?,
        title: String,
        description: String?,
        date: LocalDate,
        participants: List<Int>,
    ) {
        if (activityId != null) {
            updateActivity(activityId, title, description, date, participants)
        } else {
            insertActivity(title, description, date, participants)
        }
    }

    private suspend fun insertActivity(title: String, description: String?, date: LocalDate, participants: List<Int>) {
        /**
         * Add new entry to Room with id = max(id) + 1
         * Create new entry using API
         * Insert response into Room, should ideally have a similar ID but keep a map of local to remote ID
         */
        val activityId = contactActivitiesDao.getMaxId() + 1
        val createdDate = OffsetDateTime.now()
        val entity = ContactActivityEntity(
            activityId = activityId,
            uuid = Uuid.random(),
            title = title,
            description = description,
            date = date,
            created = createdDate,
            updated = createdDate,
            syncStatus = SyncStatus.NEW,
        )
        val crossRefs = participants
            .map { contactId ->
                ContactActivityCrossRef(
                    contactId = contactId,
                    activityId = activityId,
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

    private suspend fun updateActivity(
        activityId: Int,
        title: String,
        description: String?,
        date: LocalDate,
        participants: List<Int>,
    ) {
        val originalActivityWithParticipants = contactActivitiesDao.getActivity(activityId)
            .firstOrNull() ?: return
        val updatedActivity = originalActivityWithParticipants.activity.copy(
            title = title,
            description = description,
            date = date,
            updated = OffsetDateTime.now(),
            syncStatus = SyncStatus.EDITED,
        )
        val crossRefs = participants
            .map { participantId ->
                ContactActivityCrossRef(
                    contactId = participantId,
                    activityId = updatedActivity.activityId,
                )
            }
        contactActivitiesDao.upsert(listOf(updatedActivity), crossRefs)
        scope.launch {
            contactActivityUpdateSynchronizer.sync()
        }
    }

    suspend fun deleteActivity(activityId: Int) {
        contactActivitiesDao.setSyncStatus(activityId, SyncStatus.DELETED)
        scope.launch {
            contactActivityDeletedSynchronizer.sync()
        }
    }
}
