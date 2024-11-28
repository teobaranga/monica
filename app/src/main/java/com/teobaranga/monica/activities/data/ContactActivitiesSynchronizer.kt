package com.teobaranga.monica.activities.data

import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.data.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import timber.log.Timber

class ContactActivitiesSynchronizer @AssistedInject constructor(
    @Assisted
    private val contactId: Int,
    private val contactApi: ContactApi,
    private val contactActivitiesDao: ContactActivitiesDao,
    private val contactActivityNewSynchronizer: ContactActivityNewSynchronizer,
    private val contactActivityUpdateSynchronizer: ContactActivityUpdateSynchronizer,
    private val contactActivityDeletedSynchronizer: ContactActivityDeletedSynchronizer,
) : Synchronizer {

    override val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    override suspend fun sync() {
        if (!isSyncEnabled) {
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
                    Timber.e("Error while loading contact activities: %s", this)
                }
                .getOrElse {
                    syncState.value = Synchronizer.State.IDLE
                    return
                }
            val contactActivityEntities = contactActivitiesResponse.data
                .map {
                    it.toEntity()
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

        isSyncEnabled = false
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int): ContactActivitiesSynchronizer
    }

    companion object {

        // TODO: this doesn't make much sense, this Synchronizer is not a singleton
        private var isSyncEnabled = true
    }
}

fun ContactActivitiesResponse.ContactActivity.toEntity(): ContactActivityEntity {
    return ContactActivityEntity(
        activityId = id,
        title = summary,
        description = description,
        date = happenedAt,
        created = created,
        updated = updated,
        syncStatus = SyncStatus.UP_TO_DATE,
    )
}
