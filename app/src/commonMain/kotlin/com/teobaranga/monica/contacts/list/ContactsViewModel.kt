package com.teobaranga.monica.contacts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.contact.data.ContactSynchronizer
import com.teobaranga.monica.contact.toExternalModel
import com.teobaranga.monica.contact.userAvatar
import com.teobaranga.monica.core.data.sync.Synchronizer
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.core.ui.pulltorefresh.MonicaPullToRefreshState
import com.teobaranga.monica.user.data.UserRepository
import com.teobaranga.monica.useravatar.UserAvatar
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val PAGE_SIZE = 15

@Inject
@ContributesIntoMap(AppScope::class)
@ViewModelKey
class ContactsViewModel(
    private val dispatcher: Dispatcher,
    userRepository: UserRepository,
    contactRepository: ContactRepository,
    private val contactSynchronizer: ContactSynchronizer,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.userAvatar ?: UserAvatar.default(me.info.firstName, me.info.lastName)
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
