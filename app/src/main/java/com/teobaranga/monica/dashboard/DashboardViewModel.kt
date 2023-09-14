package com.teobaranga.monica.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.domain.contact.GetContactAvatar
import com.teobaranga.monica.settings.tokenStorage
import com.teobaranga.monica.ui.UserAvatar
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository,
    private val getContactAvatar: GetContactAvatar,
) : ViewModel() {

    var uiState by mutableStateOf<DashboardUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            userRepository.me
                .filterNotNull()
                .flatMapLatest { me ->
                    withContext(dispatcher.main) {
                        uiState = DashboardUiState(
                            userInfo = DashboardUiState.UserInfo(
                                name = me.firstName,
                            ),
                            avatar = UserAvatar(
                                initials = me.initials,
                                color = me.avatarColor,
                                data = null,
                            ),
                        )
                    }
                    getContactAvatar(me.id)
                }
                .collectLatest { photo ->
                    withContext(dispatcher.main) {
                        uiState = uiState?.run {
                            copy(avatar = avatar.copy(data = photo.data))
                        }
                    }
                }
        }
    }

    fun onClearAuthorization() {
        viewModelScope.launch(dispatcher.io) {
            // TODO Revoke current access token
            dataStore.edit { preferences ->
                preferences.tokenStorage {
                    clear()
                }
            }
        }
    }
}
