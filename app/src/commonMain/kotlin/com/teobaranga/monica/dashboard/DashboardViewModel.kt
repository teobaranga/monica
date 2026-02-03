package com.teobaranga.monica.dashboard

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
import com.teobaranga.monica.data.photo.PhotoSynchronizer
import com.teobaranga.monica.user.data.local.IUserRepository
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

private const val PAGE_SIZE = 10

@Inject
@ContributesViewModel(AppScope::class)
class DashboardViewModel(
    private val dispatcher: Dispatcher,
    userRepository: IUserRepository,
    contactRepository: ContactRepository,
    private val contactSynchronizer: ContactSynchronizer,
    private val photoSynchronizer: PhotoSynchronizer,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.avatar ?: UserAvatar.default(me.firstName)
        }
        .onStart {
            viewModelScope.launch(dispatcher.io) {
                photoSynchronizer.sync()
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
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
            viewModelScope.launch(dispatcher.io) {
                photoSynchronizer.sync()
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    val recentContacts = contactRepository.getContactsPagingData(
        orderBy = ContactRepository.OrderBy.Updated(isAscending = false),
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = 0,
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
}
