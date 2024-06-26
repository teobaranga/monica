package com.teobaranga.monica.activities.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import com.teobaranga.monica.data.sync.SyncStatus
import kotlinx.coroutines.flow.Flow

/**
 * Cache test
 */
@Dao
abstract class ContactActivitiesDao {

    @Query(
        """
        SELECT contact_activity.* FROM contact_activity
        INNER JOIN contact_activity_cross_refs ON contact_activity_cross_refs.activityId = contact_activity.activityId
        INNER JOIN contacts ON contacts.contactId = contact_activity_cross_refs.contactId
        WHERE contacts.contactId = :contactId AND NOT contact_activity.syncStatus = 'DELETED'
        ORDER BY date(date) DESC
        """,
    )
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    abstract fun getContactActivities(contactId: Int): Flow<List<ContactActivityWithParticipants>>

    @Query(
        """
        SELECT contact_activity.* FROM contact_activity
        INNER JOIN contact_activity_cross_refs ON contact_activity_cross_refs.activityId = contact_activity.activityId
        INNER JOIN contacts ON contacts.contactId = contact_activity_cross_refs.contactId
        WHERE contact_activity.activityId = :activityId
        """,
    )
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    abstract fun getActivity(activityId: Int): Flow<ContactActivityWithParticipants>

    @Query(
        """
        SELECT contact_activity.* FROM contact_activity
        INNER JOIN contact_activity_cross_refs ON contact_activity_cross_refs.activityId = contact_activity.activityId
        INNER JOIN contacts ON contacts.contactId = contact_activity_cross_refs.contactId
        WHERE contact_activity.syncStatus = :status
        ORDER BY date(date) DESC
        """,
    )
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    abstract suspend fun getActivitiesByStatus(status: SyncStatus): List<ContactActivityWithParticipants>

    @Upsert
    protected abstract suspend fun upsert(entities: List<ContactActivityEntity>)

    @Upsert
    protected abstract suspend fun upsertCrossRefs(entities: List<ContactActivityCrossRef>)

    @Transaction
    open suspend fun upsert(activities: List<ContactActivityEntity>, crossRefs: List<ContactActivityCrossRef>) {
        upsert(activities)
        deleteCrossRefs(activities.map { it.activityId })
        upsertCrossRefs(crossRefs)
    }

    @Transaction
    open suspend fun sync(entityId: Int, entry: ContactActivityEntity, crossRefs: List<ContactActivityCrossRef>) {
        deleteActivities(listOf(entityId))
        deleteCrossRefs(listOf(entityId))
        upsert(listOf(entry), crossRefs)
    }

    @Query("DELETE FROM contact_activity WHERE activityId in (:entityIds)")
    protected abstract suspend fun deleteActivities(entityIds: List<Int>)

    /**
     * Delete all the links between the given activities (mapped by ID) and their contacts.
     */
    @Query("DELETE FROM contact_activity_cross_refs WHERE activityId in (:activityIds)")
    protected abstract suspend fun deleteCrossRefs(activityIds: List<Int>)

    @Query("UPDATE contact_activity SET syncStatus = :syncStatus WHERE activityId = :activityId")
    abstract suspend fun setSyncStatus(activityId: Int, syncStatus: SyncStatus)

    @Transaction
    open suspend fun delete(activityIds: List<Int>) {
        deleteActivities(activityIds)
        deleteCrossRefs(activityIds)
    }

    @Query("SELECT max(activityId) FROM contact_activity")
    abstract suspend fun getMaxId(): Int
}
