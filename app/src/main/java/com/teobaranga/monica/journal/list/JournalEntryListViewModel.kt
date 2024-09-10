package com.teobaranga.monica.journal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.journal.data.JournalEntrySynchronizer
import com.teobaranga.monica.journal.data.JournalPagingSource
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.database.JournalEntryEntity
import com.teobaranga.monica.journal.list.ui.JournalEntryListItem
import com.teobaranga.monica.user.userAvatar
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 15

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class JournalEntryListViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    userRepository: UserRepository,
    private val journalRepository: JournalRepository,
    private val journalEntrySynchronizer: JournalEntrySynchronizer,
) : ViewModel() {

    private lateinit var pagingSource: JournalPagingSource

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.avatar ?: me.userAvatar
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val items = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE,
        ),
        pagingSourceFactory = {
            pagingSource = journalRepository.getJournalEntries(
                orderBy = JournalRepository.OrderBy.Date(isAscending = false),
            )
            pagingSource
        },
    )
        .flow
        .mapLatest { pagingData ->
            pagingData.map { journalEntryEntity ->
                journalEntryEntity.toUiModel()
            }
        }
        .cachedIn(viewModelScope)

    val isRefreshing = journalEntrySynchronizer.syncState
        .mapLatest { state ->
            state == Synchronizer.State.REFRESHING
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(dispatcher.io) {
            journalEntrySynchronizer.sync()
        }
    }

    fun onEntriesChanged() {
        pagingSource.invalidate()
    }

    private fun JournalEntryEntity.toUiModel(): JournalEntryListItem {
        return JournalEntryListItem(
            id = id,
            title = title,
            post = post,
            date = date,
        )
    }
}
