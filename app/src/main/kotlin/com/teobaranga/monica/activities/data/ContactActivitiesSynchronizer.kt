package com.teobaranga.monica.activities.data

import com.diamondedge.logging.logging
import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.core.data.sync.Synchronizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.uuid.Uuid

@Inject
class ContactActivitiesSynchronizer(
    @Assisted
    private val contactId: Int,
    private val contactApi: ContactApi,
    private val contactActivitiesDao: ContactActivitiesDao,
    private val contactActivityNewSynchronizer: ContactActivityNewSynchronizer,
    private val contactActivityUpdateSynchronizer: ContactActivityUpdateSynchronizer,
    private val contactActivityDeletedSynchronizer: ContactActivityDeletedSynchronizer,
) : Synchronizer {

    override val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    suspend fun reSync() {
        syncMap[contactId] = false
        sync()
    }

    override suspend fun sync() {
        if (syncMap.getOrPut(contactId) { false }) {
            return
        }

        syncState.value = Synchronizer.State.REFRESHING

        contactActivityNewSynchronizer.sync()

        contactActivityUpdateSynchronizer.sync()

        contactActivityDeletedSynchronizer.sync()

        // Keep track of removed contact activities, start with the full database first
        val removedIds = contactActivitiesDao.getContactActivities(contactId).first()
            .map { it.activity.activityId }
            .toMutableSet()

        var nextPage: Int? = 1
        while (nextPage != null) {
            val contactActivitiesResponse = contactApi.getContactActivities(id = contactId, page = nextPage)
                .onFailure {
                    log.e { "Error while loading contact activities: ${message()}" }
                }
                .getOrElse {
                    syncState.value = Synchronizer.State.IDLE
                    return
                }
            val contactActivityEntities = contactActivitiesResponse.data
                .map {
                    it.toEntity(
                        getUuid = { contactActivitiesDao.getUuid(it.id)}
                    )
                }
            val contactActivitiesCrossRefs = contactActivitiesResponse.data
                .flatMap { activity ->
                    activity.attendees.contacts
                        .distinctBy { contact ->
                            contact.id
                        }
                        .map { contact ->
                            ContactActivityCrossRef(
                                contactId = contact.id,
                                activityId = activity.id,
                            )
                        }
                }

            contactActivitiesDao.upsert(contactActivityEntities, contactActivitiesCrossRefs)

            contactActivitiesResponse.meta.run {
                nextPage = if (currentPage != lastPage) {
                    currentPage + 1
                } else {
                    null
                }
            }

            // Reduce the list of entries to be removed based on the entries previously inserted
            removedIds -= contactActivityEntities.map { it.activityId }.toSet()
        }

        val activityIds = removedIds.toList()
        contactActivitiesDao.delete(activityIds)

        syncState.value = Synchronizer.State.IDLE

        syncMap[contactId] = true
    }

    companion object {

        private val log = logging()

        private val syncMap = mutableMapOf<Int, Boolean>()
    }
}

suspend fun ContactActivitiesResponse.ContactActivity.toEntity(getUuid: suspend () -> Uuid?): ContactActivityEntity {
    return ContactActivityEntity(
        activityId = id,
        uuid = getUuid() ?: Uuid.parse(uuid),
        title = summary,
        description = description,
        date = happenedAt,
        created = created,
        updated = updated,
        syncStatus = SyncStatus.UP_TO_DATE,
    )
}
