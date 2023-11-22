package com.teobaranga.monica.contacts.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teobaranga.monica.contacts.model.Contact
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.database.OrderBy
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

internal class ContactPagingSource @AssistedInject constructor(
    dispatcher: Dispatcher,
    private val contactDao: ContactDao,
    private val contactSynchronizer: ContactSynchronizer,
    @Assisted
    private val orderBy: ContactRepository.OrderBy,
) : PagingSource<Int, Contact>() {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    init {
        scope.launch {
            contactSynchronizer.syncState
                .reduce { prev, new ->
                    if (prev == Synchronizer.State.REFRESHING && new == Synchronizer.State.IDLE) {
                        scope.cancel()
                        invalidate()
                    }
                    new
                }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contact> {
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

    override fun getRefreshKey(state: PagingState<Int, Contact>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun getEntries(start: Int, params: LoadParams<Int>): List<Contact> {
        return contactDao.getContacts(
            orderBy = with(orderBy) { OrderBy(columnName, isAscending) },
            limit = params.loadSize,
            offset = start * params.loadSize,
        ).map { contactEntities ->
            contactEntities.map {
                it.toExternalModel()
            }
        }.first()
    }


    @AssistedFactory
    internal interface Factory {
        fun create(orderBy: ContactRepository.OrderBy): ContactPagingSource
    }
}
