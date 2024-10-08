package com.teobaranga.monica.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.ContactSynchronizer
import com.teobaranga.monica.data.photo.PhotoSynchronizer
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.user.userAvatar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val PAGE_SIZE = 10

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    contactRepository: ContactRepository,
    private val contactSynchronizer: ContactSynchronizer,
    private val photoSynchronizer: PhotoSynchronizer,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.avatar ?: me.userAvatar
        }
        .onStart {
            photoSynchronizer.sync()
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val userUiState = userRepository.me
        .mapLatest { me ->
            UserUiState(
                userInfo = UserUiState.UserInfo(
                    name = me.firstName,
                ),
            )
        }
        .onStart {
            userRepository.sync()
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
        .onStart {
            contactSynchronizer.sync()
        }
        .cachedIn(viewModelScope)
}
