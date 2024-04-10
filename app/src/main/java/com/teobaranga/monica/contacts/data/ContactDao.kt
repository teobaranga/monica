package com.teobaranga.monica.contacts.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.teobaranga.monica.data.photo.ContactPhotos
import com.teobaranga.monica.database.OrderBy
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContactDao {

    fun getContacts(
        orderBy: OrderBy? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): Flow<List<ContactEntity>> {
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

    @Transaction
    @Query("SELECT contactId, avatarUrl FROM contacts WHERE contactId = :contactId")
    abstract fun getContactPhotos(contactId: Int): Flow<ContactPhotos>

    @Upsert
    abstract suspend fun upsertContacts(entities: List<ContactEntity>)

    @Query("DELETE FROM contacts WHERE contactId in (:entityIds)")
    abstract suspend fun delete(entityIds: List<Int>)
}
