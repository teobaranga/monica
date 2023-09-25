package com.teobaranga.monica.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.userAvatar
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.destinations.DirectionDestination
import com.teobaranga.monica.home.HomeNavigationManager
import com.teobaranga.monica.user.userAvatar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val homeNavigationManager: HomeNavigationManager,
    userRepository: UserRepository,
    contactRepository: ContactRepository,
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

    val recentContactsUiState = contactRepository.getContacts(
        orderBy = ContactRepository.OrderBy.Updated(isAscending = false),
    )
        .mapLatest { contacts ->
            val avatars = contacts.take(10)
                .map { contact ->
                    contact.userAvatar
                }
            RecentContactsUiState(
                contacts = avatars,
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    fun navigateTo(destination: DirectionDestination) {
        homeNavigationManager.navigateTo(destination)
    }
}
