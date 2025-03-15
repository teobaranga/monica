package com.teobaranga.monica.contacts.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.teobaranga.monica.core.data.local.OrderBy
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.data.photo.ContactPhotos
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactDao {

    fun getContacts(orderBy: OrderBy? = null, limit: Int? = null, offset: Int? = null): Flow<List<ContactEntity>> {
        val query = buildString {
            append("SELECT * FROM contacts")
            if (orderBy != null) {
                append(" ")
                append("ORDER BY ${orderBy.columnName} ${if (orderBy.isAscending) "ASC" else "DESC"}")
            }
            if (limit != null) {
                append(" ")
                append("LIMIT $limit")
            }
            if (offset != null) {
                append(" ")
                append("OFFSET $offset")
            }
        }
        return getContacts(SimpleSQLiteQuery(query))
    }

    @RawQuery(observedEntities = [ContactEntity::class])
    protected abstract fun getContacts(query: SupportSQLiteQuery): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE contactId in (:entityIds)")
    abstract fun getContacts(entityIds: List<Int>): Flow<List<ContactEntity>>

    @Query("SELECT contactId FROM contacts")
    abstract fun getContactIds(): Flow<List<Int>>

    @Query(
        value = """
        SELECT * FROM contacts
        WHERE contactId = :id
    """,
    )
    abstract fun getContact(id: Int): Flow<ContactEntity>

    @Query("SELECT * FROM contacts WHERE completeName LIKE '%' || :query || '%' AND contactId NOT IN (:excludeIds)")
    abstract fun searchContacts(query: String, excludeIds: List<Int> = emptyList()): Flow<List<ContactEntity>>

    @Transaction
    @Query("SELECT contactId, avatar_url FROM contacts WHERE contactId = :contactId")
    abstract fun getContactPhotos(contactId: Int): Flow<ContactPhotos>

    @Upsert
    abstract suspend fun upsertContacts(entities: List<ContactEntity>)

    @Query("DELETE FROM contacts WHERE contactId in (:entityIds)")
    abstract suspend fun delete(entityIds: List<Int>)

    @Query("SELECT max(contactId) FROM contacts")
    abstract suspend fun getMaxId(): Int

    @Query("SELECT * FROM contacts WHERE syncStatus = :status")
    abstract suspend fun getBySyncStatus(status: SyncStatus): List<ContactEntity>

    @Query("UPDATE contacts SET syncStatus = :syncStatus WHERE contactId = :contactId")
    abstract suspend fun setSyncStatus(contactId: Int, syncStatus: SyncStatus)

    @Transaction
    open suspend fun sync(entityId: Int, contact: ContactEntity) {
        delete(listOf(entityId))
        upsertContacts(listOf(contact))
    }
}
