package com.teobaranga.monica.journal.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.teobaranga.monica.database.OrderBy
import kotlinx.coroutines.flow.Flow

@Dao
abstract class JournalDao {

    fun getJournalEntries(orderBy: OrderBy? = null): Flow<List<JournalEntryEntity>> {
        val query = buildString {
            append("SELECT * FROM journal_entries")
            if (orderBy != null) {
                append(" ")
                append("ORDER BY ${orderBy.columnName} ${if (orderBy.isAscending) "ASC" else "DESC"}")
            }
        }
        return getJournalEntries(SimpleSQLiteQuery(query))
    }

    @RawQuery(observedEntities = [JournalEntryEntity::class])
    protected abstract fun getJournalEntries(query: SupportSQLiteQuery): Flow<List<JournalEntryEntity>>

    @Query(
        value = """
        SELECT * FROM journal_entries
        WHERE id = :id
    """,
    )
    abstract fun getJournalEntry(id: Int): Flow<JournalEntryEntity>

    @Upsert
    abstract suspend fun upsertJournalEntries(entities: List<JournalEntryEntity>)
}
