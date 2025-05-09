package com.teobaranga.monica.database

import com.teobaranga.monica.activities.data.ContactActivitiesDao
import com.teobaranga.monica.activities.data.ContactActivityCrossRef
import com.teobaranga.monica.activities.data.ContactActivityEntity
import com.teobaranga.monica.activities.data.ContactActivityWithParticipants
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.data.DaosComponent
import io.mockk.every
import io.mockk.mockk
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
    replaces = [DaosComponent::class],
)
class ContactActivitiesDao : ContactActivitiesDao() {

    private val activities = MutableStateFlow<List<ContactActivityEntity>>(emptyList())

    private val activitiesWithParticipants = MutableStateFlow<List<ContactActivityWithParticipants>>(emptyList())

    override fun getContactActivities(contactId: Int): Flow<List<ContactActivityWithParticipants>> {
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
        activitiesWithParticipants.value = entities.map { crossRef ->
            val activity = activities.value.first { it.activityId == crossRef.activityId }
            val contact = mockk<ContactEntity> {
                every { contactId } returns crossRef.contactId
            }
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
