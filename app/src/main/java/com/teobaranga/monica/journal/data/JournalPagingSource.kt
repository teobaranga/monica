package com.teobaranga.monica.journal.data

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.database.OrderBy
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity
import com.teobaranga.monica.paging.RoomPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

internal class JournalPagingSource @AssistedInject constructor(
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

    @AssistedFactory
    internal interface Factory {
        fun create(orderBy: JournalRepository.OrderBy): JournalPagingSource
    }
}
