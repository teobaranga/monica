package com.teobaranga.monica.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.ContactSynchronizer
import com.teobaranga.monica.contacts.data.toExternalModel
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.sync.Synchronizer
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.ui.pulltorefresh.MonicaPullToRefreshState
import com.teobaranga.monica.user.userAvatar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

private const val PAGE_SIZE = 15

@Inject
@ContributesViewModel(AppScope::class)
class ContactsViewModel internal constructor(
    private val dispatcher: Dispatcher,
    userRepository: UserRepository,
    contactRepository: ContactRepository,
    private val contactSynchronizer: ContactSynchronizer,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.avatar ?: me.userAvatar
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    val items = contactRepository.getContactsPagingData(
        orderBy = ContactRepository.OrderBy.Name(isAscending = true),
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE,
        ),
    )
        .mapLatest { pagingData ->
            pagingData.map { contactEntity ->
                contactEntity.toExternalModel()
            }
        }
        .onStart {
            viewModelScope.launch(dispatcher.io) {
                contactSynchronizer.sync()
            }
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
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    val state = ContactsUiState(
        items = items,
    )

    fun refresh() {
        viewModelScope.launch {
            contactSynchronizer.reSync()
        }
    }
}
