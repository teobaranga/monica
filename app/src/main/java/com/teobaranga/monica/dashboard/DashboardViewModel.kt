package com.teobaranga.monica.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.destinations.DirectionDestination
import com.teobaranga.monica.home.HomeNavigationManager
import com.teobaranga.monica.ui.UserAvatar
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val userRepository: UserRepository,
    private val homeNavigationManager: HomeNavigationManager,
) : ViewModel() {

    var uiState by mutableStateOf<DashboardUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            userRepository.me
                .filterNotNull()
                .collectLatest { me ->
                    withContext(dispatcher.main) {
                        val avatar = if (me.contact != null) {
                            UserAvatar(
                                initials = me.contact.initials,
                                color = me.contact.avatarColor,
                                data = me.contact.avatarData,
                            )
                        } else {
                            UserAvatar(
                                initials = me.firstName.take(2).uppercase(),
                                color = "#709512",
                                data = null,
                            )
                        }
                        uiState = DashboardUiState(
                            userInfo = DashboardUiState.UserInfo(
                                name = me.firstName,
                            ),
                            avatar = avatar,
                        )
                    }
                }
        }
    }

    fun navigateTo(destination: DirectionDestination) {
        homeNavigationManager.navigateTo(destination)
    }
}
