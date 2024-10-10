package com.teobaranga.monica.journal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.journal.data.JournalEntrySynchronizer
import com.teobaranga.monica.journal.data.JournalRepository
import com.teobaranga.monica.journal.database.JournalEntryEntity
import com.teobaranga.monica.journal.list.ui.JournalEntryListItem
import com.teobaranga.monica.ui.pulltorefresh.MonicaPullToRefreshState
import com.teobaranga.monica.user.userAvatar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class JournalEntryListViewModel @Inject constructor(
    userRepository: UserRepository,
    journalRepository: JournalRepository,
    private val journalEntrySynchronizer: JournalEntrySynchronizer,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.avatar ?: me.userAvatar
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val items = journalRepository.getJournalEntriesPagingData()
        .mapLatest { pagingData ->
            pagingData.map { journalEntryEntity ->
                journalEntryEntity.toUiModel()
            }
        }
        .onStart {
            journalEntrySynchronizer.sync()
        }
        .cachedIn(viewModelScope)

    private val _refreshState = MonicaPullToRefreshState(onRefresh = ::refresh)
    val refreshState = journalEntrySynchronizer.syncState
        .mapLatest { state ->
            state == Synchronizer.State.REFRESHING
        }
        .map {
            _refreshState.apply {
                isRefreshing = it
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = _refreshState,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    private fun refresh() {
        viewModelScope.launch {
            journalEntrySynchronizer.reSync()
        }
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
