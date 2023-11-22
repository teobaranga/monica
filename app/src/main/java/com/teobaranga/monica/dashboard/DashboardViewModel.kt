package com.teobaranga.monica.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.ContactSynchronizer
import com.teobaranga.monica.contacts.userAvatar
import com.teobaranga.monica.data.photo.PhotoSynchronizer
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.destinations.DirectionDestination
import com.teobaranga.monica.home.HomeNavigationManager
import com.teobaranga.monica.user.userAvatar
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 10

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val homeNavigationManager: HomeNavigationManager,
    userRepository: UserRepository,
    contactRepository: ContactRepository,
    private val contactSynchronizer: ContactSynchronizer,
    private val photoSynchronizer: PhotoSynchronizer,
) : ViewModel() {

    val userUiState = userRepository.me
        .mapLatest { me ->
            val avatar = me.contact?.userAvatar ?: me.userAvatar
            UserUiState(
                userInfo = UserUiState.UserInfo(
                    name = me.firstName,
                ),
                avatar = avatar,
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val recentContacts = Pager(
        // Limit to *only* PAGE_SIZE results
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = 0,
            initialLoadSize = PAGE_SIZE,
        ),
        pagingSourceFactory = {
            contactRepository.getContacts(
                orderBy = ContactRepository.OrderBy.Updated(isAscending = false),
            )
        },
    )
        .flow
        .cachedIn(viewModelScope)

    fun navigateTo(destination: DirectionDestination) {
        homeNavigationManager.navigateTo(destination)
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(dispatcher.io) {
            contactSynchronizer.sync()
        }
        viewModelScope.launch(dispatcher.io) {
            photoSynchronizer.sync()
        }
    }
}
