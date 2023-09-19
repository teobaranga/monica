package com.teobaranga.monica.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.data.contact.ContactRepository
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.destinations.DirectionDestination
import com.teobaranga.monica.home.HomeNavigationManager
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
                .collectLatest { me ->
                    val avatar = if (me.contact != null) {
                        UserAvatar(
                            contactId = me.contact.id,
                            initials = me.contact.initials,
                            color = me.contact.avatarColor,
                        )
                    } else {
                        UserAvatar(
                            contactId = -1,
                            initials = me.firstName.take(2).uppercase(),
                            color = "#709512",
                        )
                    }
                    withContext(dispatcher.main) {
                        userUiState = UserUiState(
                            userInfo = UserUiState.UserInfo(
                                name = me.firstName,
                            ),
                            avatar = avatar,
                        )
                    }
                }
        }
        viewModelScope.launch(dispatcher.io) {
            contactRepository.getContacts()
                .collectLatest {
                    val userAvatars = it.take(10)
                        .map {
                            UserAvatar(
                                contactId = it.id,
                                initials = it.initials,
                                color = it.avatarColor,
                            )
                        }
                    withContext(dispatcher.main) {
                        recentContactsUiState = RecentContactsUiState(
                            contacts = userAvatars,
                        )
                    }
                }
        }
    }

    fun navigateTo(destination: DirectionDestination) {
        homeNavigationManager.navigateTo(destination)
    }
}
