package com.teobaranga.monica.journal.data

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.diamondedge.logging.logging
import com.teobaranga.monica.core.data.sync.SyncStatus
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Instant
import kotlin.uuid.Uuid

private const val PAGE_SIZE = 15

@Inject
@SingleIn(AppScope::class)
class JournalRepository(
    private val dispatcher: Dispatcher,
    private val getNow: () -> Instant,
    private val journalDao: JournalDao,
    private val journalPagingSourceFactory: (OrderBy) -> JournalPagingSource,
    private val journalEntryNewSynchronizer: JournalEntryNewSynchronizer,
    private val journalEntryUpdateSynchronizer: JournalEntryUpdateSynchronizer,
    private val journalEntryDeletedSynchronizer: JournalEntryDeletedSynchronizer,
) {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val pagingSourceFactoryMap =
        mutableMapOf<OrderBy, InvalidatingPagingSourceFactory<Int, JournalEntryEntity>>()

    fun getJournalEntriesPagingData(
        orderBy: OrderBy = OrderBy.Date(isAscending = false),
    ): Flow<PagingData<JournalEntryEntity>> {
        val pagingSourceFactory = pagingSourceFactoryMap.getOrPut(orderBy) {
            InvalidatingPagingSourceFactory {
                journalPagingSourceFactory(orderBy)
            }
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE,
            ),
            pagingSourceFactory = pagingSourceFactory,
        ).flow
    }

    private fun invalidatePages() {
        pagingSourceFactoryMap.values
            .forEach { invalidatingPagingSourceFactory ->
                invalidatingPagingSourceFactory.invalidate()
            }
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
        withContext(dispatcher.default) {
            /**
             * Add new entry to Room with id = max(id) + 1
             * Create new entry using API
             * Insert response into Room, should ideally have a similar ID but keep a map of local to remote ID
             */
            val localId = journalDao.getMaxId() + 1
            val createdDate = getNow()
            val entry = JournalEntryEntity(
                id = localId,
                uuid = Uuid.random(),
                title = title,
                post = post,
                date = date,
                created = createdDate,
                updated = createdDate,
                syncStatus = SyncStatus.NEW,
            )
            log.debug { "Inserting new journal entry: $entry" }
            journalDao.upsertJournalEntry(entry)
            invalidatePages()
            scope.launch {
                journalEntryNewSynchronizer.sync()
                invalidatePages()
            }
        }
    }

    private suspend fun updateJournalEntry(entryId: Int, title: String?, post: String, date: LocalDate) {
        withContext(dispatcher.default) {
            val originalEntry = journalDao.getJournalEntry(entryId).firstOrNull() ?: return@withContext
            val updatedEntry = originalEntry.copy(
                title = title,
                post = post,
                date = date,
                updated = getNow(),
                syncStatus = SyncStatus.EDITED,
            )
            journalDao.upsertJournalEntry(updatedEntry)
            invalidatePages()
            scope.launch {
                journalEntryUpdateSynchronizer.sync()
                invalidatePages()
            }
        }
    }

    suspend fun deleteJournalEntry(entryId: Int) {
        withContext(dispatcher.default) {
            journalDao.setSyncStatus(entryId, SyncStatus.DELETED)
            invalidatePages()
            scope.launch {
                journalEntryDeletedSynchronizer.sync()
                invalidatePages()
            }
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

    companion object {
        private val log = logging()
    }
}
