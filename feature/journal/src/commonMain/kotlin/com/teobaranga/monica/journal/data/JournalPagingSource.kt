package com.teobaranga.monica.journal.data

import com.teobaranga.monica.core.data.local.OrderBy
import com.teobaranga.monica.core.data.local.RoomPagingSource
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.journal.data.local.JournalDao
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.first

@AssistedInject
class JournalPagingSource(
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
