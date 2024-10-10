package com.teobaranga.monica.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.teobaranga.monica.contacts.data.ContactPagingSource
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.ContactSynchronizer
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.data.user.UserRepository
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

private const val PAGE_SIZE = 15

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class ContactsViewModel @Inject constructor(
    userRepository: UserRepository,
    contactRepository: ContactRepository,
    private val contactSynchronizer: ContactSynchronizer,
) : ViewModel() {

    private lateinit var pagingSource: ContactPagingSource

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
            contactRepository.getContacts(
                orderBy = ContactRepository.OrderBy.Name(isAscending = true),
            ).also {
                pagingSource = it
            }
        },
    )
        .flow
        .onStart {
            contactSynchronizer.sync()
        }
        .cachedIn(viewModelScope)

    private val _refreshState = MonicaPullToRefreshState(onRefresh = ::refresh)
    val refreshState = contactSynchronizer.syncState
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

    val state = ContactsUiState(
        items = items,
    )

    fun refresh() {
        viewModelScope.launch {
            contactSynchronizer.reSync()
        }
    }

    fun onEntriesChanged() {
        pagingSource.invalidate()
    }
}
