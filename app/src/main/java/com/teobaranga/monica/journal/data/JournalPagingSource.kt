package com.teobaranga.monica.journal.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.database.OrderBy
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.toExternalModel
import com.teobaranga.monica.journal.model.JournalEntry
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch


private const val STARTING_KEY = 0

internal class JournalPagingSource @AssistedInject constructor(
    dispatcher: Dispatcher,
    private val journalDao: JournalDao,
    private val journalSynchronizer: JournalSynchronizer,
    @Assisted
    private val orderBy: JournalRepository.OrderBy,
) : PagingSource<Int, JournalEntry>() {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    init {
        scope.launch {
            journalSynchronizer.syncState
                .reduce { prev, new ->
                    if (prev == Synchronizer.State.REFRESHING && new == Synchronizer.State.IDLE) {
                        scope.cancel()
                        invalidate()
                    }
                    new
                }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JournalEntry> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY

        val entries = getEntries(start, params)

        return LoadResult.Page(
            data = entries,
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = if (start == 0) null else start - 1,
            nextKey = if (entries.isEmpty()) null else start + 1,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, JournalEntry>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun getEntries(start: Int, params: LoadParams<Int>): List<JournalEntry> {
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
