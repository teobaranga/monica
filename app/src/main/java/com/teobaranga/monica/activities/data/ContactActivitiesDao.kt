package com.teobaranga.monica.activities.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactActivitiesDao {

    @Query(
        """
        SELECT
            contact_activity.activityId,
            contact_activity.description,
            contact_activity.title,
            contact_activity.date
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

    @Upsert
    abstract suspend fun upsert(entities: List<ContactActivityEntity>)

    @Upsert
    abstract suspend fun upsertCrossRefs(entities: List<ContactActivityCrossRef>)

    @Query("DELETE FROM contact_activity WHERE activityId in (:entityIds)")
    abstract suspend fun delete(entityIds: List<Int>)

    @Query("DELETE FROM contact_activity_cross_refs WHERE activityId in (:entityIds)")
    abstract suspend fun deleteCrossRefs(entityIds: List<Int>)
}
