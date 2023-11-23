package com.teobaranga.monica.journal.data

import com.teobaranga.monica.database.OrderBy
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.toExternalModel
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.paging.RoomPagingSource
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class JournalPagingSource @AssistedInject constructor(
    dispatcher: Dispatcher,
    journalSynchronizer: JournalSynchronizer,
    private val journalDao: JournalDao,
    @Assisted
    private val orderBy: JournalRepository.OrderBy,
) : RoomPagingSource<JournalEntry>(
    dispatcher = dispatcher,
    synchronizer = journalSynchronizer,
) {

    override suspend fun getEntries(start: Int, params: LoadParams<Int>): List<JournalEntry> {
        return journalDao.getJournalEntries(
            orderBy = with(orderBy) { OrderBy(columnName, isAscending) },
            limit = params.loadSize,
            offset = start * params.loadSize,
        ).map { journalEntryEntities ->
            journalEntryEntities.map {
                it.toExternalModel()
            }
        }.first()
    }


    @AssistedFactory
    internal interface Factory {
        fun create(orderBy: JournalRepository.OrderBy): JournalPagingSource
    }
}
