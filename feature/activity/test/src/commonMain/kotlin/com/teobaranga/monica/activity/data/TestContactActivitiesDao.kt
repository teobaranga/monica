package com.teobaranga.monica.activity.data

import com.teobaranga.monica.activity.data.di.ActivityDaoComponent
import com.teobaranga.monica.contact.data.local.ContactEntity
import com.teobaranga.monica.contact.data.local.ContactEntityAvatar
import com.teobaranga.monica.core.data.sync.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.uuid.Uuid

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    replaces = [ActivityDaoComponent::class],
)
class TestContactActivitiesDao : ContactActivitiesDao() {

    private val activities = MutableStateFlow<List<ContactActivityEntity>>(emptyList())

    private val activitiesWithParticipants = MutableStateFlow<List<ContactActivityWithParticipants>>(emptyList())

    override fun getContactActivities(contactId: Int): Flow<List<ContactActivityWithParticipants>> {
        // TODO: use crossrefs to combine activities and participants by looking up the TestContactDao
        return activitiesWithParticipants
    }

    override fun getActivity(activityId: Int): Flow<ContactActivityWithParticipants> {
        TODO("Not yet implemented")
    }

    override suspend fun getActivitiesByStatus(status: SyncStatus): List<ContactActivityWithParticipants> {
        return activitiesWithParticipants.value.filter { it.activity.syncStatus == status }
    }

    override suspend fun upsert(entities: List<ContactActivityEntity>) {
        activities.value = entities
    }

    override suspend fun upsertCrossRefs(entities: List<ContactActivityCrossRef>) {
        // TODO: fix this - only store crossrefs here
        activitiesWithParticipants.value = entities.map { crossRef ->
            val activity = activities.value.first { it.activityId == crossRef.activityId }
            val contact = ContactEntity(
                contactId = crossRef.contactId,
                firstName = "First",
                lastName = "Last",
                nickname = null,
                completeName = "First Last",
                initials = "FI",
                avatar = ContactEntityAvatar(
                    url = null,
                    color = "#000000",
                ),
                birthdate = null,
                genderId = null,
                updated = null,
                syncStatus = SyncStatus.UP_TO_DATE,
            )
            ContactActivityWithParticipants(
                activity = activity,
                participants = listOf(contact),
            )
        }
    }

    override suspend fun deleteActivities(entityIds: List<Int>) {
        activities.update {
            it.filterNot { it.activityId in entityIds }
        }
    }

    override suspend fun deleteCrossRefs(activityIds: List<Int>) {
        activitiesWithParticipants.update {
            it.filterNot { it.activity.activityId in activityIds }
        }
    }

    override suspend fun setSyncStatus(activityId: Int, syncStatus: SyncStatus) {
        TODO("Not yet implemented")
    }

    override suspend fun getMaxId(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getUuid(activityId: Int): Uuid? {
        TODO("Not yet implemented")
    }
}
