package com.teobaranga.monica.activities.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import com.teobaranga.monica.data.sync.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactActivitiesDao {

    @Query(
        """
        SELECT
            contact_activity.activityId,
            contact_activity.description,
            contact_activity.title,
            contact_activity.date,
            contact_activity.created,
            contact_activity.updated,
            contact_activity.syncStatus
        FROM contact_activity
        INNER JOIN contact_activity_cross_refs ON contact_activity_cross_refs.activityId = contact_activity.activityId
        INNER JOIN contacts ON contacts.contactId = contact_activity_cross_refs.contactId
        WHERE contacts.contactId = :contactId
        ORDER BY date(date) DESC
        """,
    )
    @RewriteQueriesToDropUnusedColumns
    @Transaction
    abstract fun getContactActivities(contactId: Int): Flow<List<ContactActivityWithParticipants>>

    @Query("SELECT * FROM contact_activity WHERE activityId = :activityId")
    abstract fun getActivity(activityId: Int): Flow<ContactActivityEntity>

    @Query(
        """
        SELECT
            contact_activity.activityId,
            contact_activity.description,
            contact_activity.title,
            contact_activity.date,
            contact_activity.created,
            contact_activity.updated,
            contact_activity.syncStatus
        FROM contact_activity
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

    @Query("DELETE FROM contact_activity_cross_refs WHERE activityId in (:entityIds)")
    protected abstract suspend fun deleteCrossRefs(entityIds: List<Int>)

    @Transaction
    open suspend fun delete(activityIds: List<Int>) {
        deleteActivities(activityIds)
        deleteCrossRefs(activityIds)
    }

    @Query("SELECT max(activityId) FROM contact_activity")
    abstract suspend fun getMaxId(): Int
}
