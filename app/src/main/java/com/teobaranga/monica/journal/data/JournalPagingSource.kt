package com.teobaranga.monica.journal.data

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.database.OrderBy
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import com.teobaranga.monica.paging.RoomPagingSource
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
internal class JournalPagingSource(
    dispatcher: Dispatcher,
    journalEntrySynchronizer: JournalEntrySynchronizer,
    private val journalDao: JournalDao,
    @Assisted
    private val orderBy: JournalRepository.OrderBy,
) : RoomPagingSource<JournalEntryEntity>(
    dispatcher = dispatcher,
    synchronizer = journalEntrySynchronizer,
) {

    override suspend fun getEntries(start: Int, params: LoadParams<Int>): List<JournalEntryEntity> {
        return journalDao.getJournalEntries(
            orderBy = with(orderBy) { OrderBy(columnName, isAscending) },
            limit = params.loadSize,
            offset = start * params.loadSize,
        ).first()
    }
}
