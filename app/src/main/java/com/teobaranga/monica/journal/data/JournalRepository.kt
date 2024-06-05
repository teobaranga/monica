package com.teobaranga.monica.journal.data

import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class JournalRepository @Inject constructor(
    private val journalDao: JournalDao,
    private val pagingSource: Provider<JournalPagingSource.Factory>,
) {
    fun getJournalEntries(orderBy: OrderBy): JournalPagingSource {
        return pagingSource.get().create(orderBy)
    }

    fun getJournalEntry(id: Int): Flow<JournalEntryEntity> {
        return journalDao.getJournalEntry(id)
    }

    suspend fun createJournalEntry(title: String?, post: String, date: OffsetDateTime) {
        /**
         * Add new entry to Room with id = max(id) + 1
         * Create new entry using API
         * Insert response into Room, should ideally have a similar ID but keep a map of local to remote ID
         */
        val localId = journalDao.getMaxId() + 1
        val createdDate = OffsetDateTime.now()
        val entry = JournalEntryEntity(
            id = localId,
            uuid = UUID.randomUUID(),
            title = title,
            post = post,
            date = date
                .withSecond(0)
                .withMinute(0)
                .withHour(0),
            created = createdDate,
            updated = createdDate,
            syncStatus = SyncStatus.NEW,
        )
        journalDao.insertJournalEntry(entry)
    }

    sealed interface OrderBy {

        val columnName: String

        val isAscending: Boolean

        data class Updated(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "datetime(updated)"
        }

        data class Date(
            override val isAscending: Boolean,
        ) : OrderBy {
            override val columnName = "datetime(date)"
        }
    }
}
