package com.teobaranga.monica.activities.data

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.contacts.data.ContactApi
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
) : Synchronizer {

    override val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    override suspend fun sync() {
        syncState.value = Synchronizer.State.REFRESHING

        // Keep track of removed contact activities, start with the full database first
        val removedIds = contactActivitiesDao.getContactActivities(contactId).first()
            .map { it.activityId }
            .toMutableSet()

        var nextPage: Int? = 1
        while (nextPage != null) {
            val contactActivitiesResponse = contactApi.getContactActivities(id = contactId, page = nextPage)
                .onFailure {
                    Timber.w("Error while loading contact activities: %s", this)
                }
                .getOrNull() ?: break
            val contactActivityEntities = contactActivitiesResponse.data
                .map {
                    it.toEntity()
                }
            val contactActivitiesCrossRefs = contactActivityEntities
                .map { activityEntity ->
                    ContactActivityCrossRef(
                        contactId = contactId,
                        activityId = activityEntity.activityId,
                    )
                }

            contactActivitiesDao.upsert(contactActivityEntities)
            contactActivitiesDao.upsertCrossRefs(contactActivitiesCrossRefs)

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
        contactActivitiesDao.deleteCrossRefs(activityIds)

        syncState.value = Synchronizer.State.IDLE
    }

    private fun ContactActivitiesResponse.ContactActivity.toEntity(): ContactActivityEntity {
        return ContactActivityEntity(
            activityId = id,
            title = summary,
            description = description,
            date = happenedAt,
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int): ContactActivitiesSynchronizer
    }
}
