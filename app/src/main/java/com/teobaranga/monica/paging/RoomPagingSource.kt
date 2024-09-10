package com.teobaranga.monica.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.sync.Synchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch

private const val STARTING_KEY = 0

abstract class RoomPagingSource<T : Any>(
    dispatcher: Dispatcher,
    private val synchronizer: Synchronizer,
) : PagingSource<Int, T>() {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    init {
        scope.launch {
            synchronizer.syncState
                .reduce { prev, new ->
                    if (prev == Synchronizer.State.REFRESHING && new == Synchronizer.State.IDLE) {
                        scope.cancel()
                        invalidate()
                    }
                    new
                }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY

        val entries = getEntries(start, params)

        return LoadResult.Page(
            data = entries,
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = if (start == STARTING_KEY) null else start - 1,
            nextKey = if (entries.isEmpty()) null else start + 1,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    protected abstract suspend fun getEntries(start: Int, params: LoadParams<Int>): List<T>
}
