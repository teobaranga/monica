package com.teobaranga.monica.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.userAvatar
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.destinations.DirectionDestination
import com.teobaranga.monica.home.HomeNavigationManager
import com.teobaranga.monica.user.userAvatar
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val homeNavigationManager: HomeNavigationManager,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    var userUiState by mutableStateOf<UserUiState?>(null)

    var recentContactsUiState by mutableStateOf<RecentContactsUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            userRepository.me
                .mapLatest { me ->
                    val avatar = me.contact?.userAvatar ?: me.userAvatar
                    UserUiState(
                        userInfo = UserUiState.UserInfo(
                            name = me.firstName,
                        ),
                        avatar = avatar,
                    )
                }
                .flowOn(dispatcher.main)
                .collectLatest { uiState ->
                    userUiState = uiState
                }
        }
        viewModelScope.launch(dispatcher.io) {
            contactRepository.getContacts(orderBy = ContactRepository.OrderBy.Updated(isAscending = false))
                .mapLatest { contacts ->
                    val avatars = contacts.take(10)
                        .map { contact ->
                            contact.userAvatar
                        }
                    RecentContactsUiState(
                        contacts = avatars,
                    )
                }
                .flowOn(dispatcher.main)
                .collectLatest { uiState ->
                    recentContactsUiState = uiState
                }
        }
    }

    fun navigateTo(destination: DirectionDestination) {
        homeNavigationManager.navigateTo(destination)
    }
}
