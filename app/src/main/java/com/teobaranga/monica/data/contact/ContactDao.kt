package com.teobaranga.monica.data.contact

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

    fun getContacts(orderBy: OrderBy? = null): Flow<List<ContactEntity>> {
        val query = buildString {
            append("SELECT * FROM contacts")
            if (orderBy != null) {
                append(" ")
                append("ORDER BY ${orderBy.columnName} ${if (orderBy.isAscending) "ASC" else "DESC"}")
            }
        }
        return getContacts(SimpleSQLiteQuery(query))
    }

    @RawQuery(observedEntities = [ContactEntity::class])
    protected abstract fun getContacts(query: SupportSQLiteQuery): Flow<List<ContactEntity>>

    @Query(
        value = """
        SELECT * FROM contacts
        WHERE id = :id
    """,
    )
    abstract fun getContact(id: Int): Flow<ContactEntity>

    @Transaction
    @Query("SELECT id, avatarUrl FROM contacts WHERE id = :contactId")
    abstract fun getContactPhotos(contactId: Int): Flow<ContactPhotos>

    @Upsert
    abstract suspend fun upsertContacts(entities: List<ContactEntity>)
}
