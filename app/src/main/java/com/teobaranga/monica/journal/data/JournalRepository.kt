package com.teobaranga.monica.journal.data

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.sync.SyncStatus
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class JournalRepository @Inject constructor(
    dispatcher: Dispatcher,
    private val journalDao: JournalDao,
    private val pagingSource: Provider<JournalPagingSource.Factory>,
    private val journalEntryNewSynchronizer: JournalEntryNewSynchronizer,
    private val journalEntryUpdateSynchronizer: JournalEntryUpdateSynchronizer,
    private val journalEntryDeletedSynchronizer: JournalEntryDeletedSynchronizer,
) {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    fun getJournalEntries(orderBy: OrderBy): JournalPagingSource {
        return pagingSource.get().create(orderBy)
    }

    fun getJournalEntry(id: Int): Flow<JournalEntryEntity> {
        return journalDao.getJournalEntry(id)
    }

    suspend fun upsertJournalEntry(entryId: Int?, title: String?, post: String, date: LocalDate) {
        if (entryId != null) {
            updateJournalEntry(entryId, title, post, date)
        } else {
            insertJournalEntry(title, post, date)
        }
    }

    private suspend fun insertJournalEntry(title: String?, post: String, date: LocalDate) {
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
            date = date,
            created = createdDate,
            updated = createdDate,
            syncStatus = SyncStatus.NEW,
        )
        journalDao.upsertJournalEntry(entry)
        scope.launch {
            journalEntryNewSynchronizer.sync()
        }
    }

    private suspend fun updateJournalEntry(entryId: Int, title: String?, post: String, date: LocalDate) {
        val originalEntry = journalDao.getJournalEntry(entryId).firstOrNull() ?: return
        val updatedEntry = originalEntry.copy(
            title = title,
            post = post,
            date = date,
            updated = OffsetDateTime.now(),
            syncStatus = SyncStatus.EDITED,
        )
        journalDao.upsertJournalEntry(updatedEntry)
        scope.launch {
            journalEntryUpdateSynchronizer.sync()
        }
    }

    suspend fun deleteJournalEntry(entryId: Int) {
        journalDao.setSyncStatus(entryId, SyncStatus.DELETED)
        scope.launch {
            journalEntryDeletedSynchronizer.sync()
        }
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
