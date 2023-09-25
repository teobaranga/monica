package com.teobaranga.monica.contacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.ui.avatar.UserAvatar
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
class ContactsViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    var userAvatar by mutableStateOf<UserAvatar?>(null)

    var uiState by mutableStateOf<ContactsUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            userRepository.me
                .mapLatest { me ->
                    me.contact?.userAvatar ?: me.userAvatar
                }
                .flowOn(dispatcher.main)
                .collectLatest { avatar ->
                    userAvatar = avatar
                }
        }
        viewModelScope.launch(dispatcher.io) {
            contactRepository.getContacts()
                .mapLatest { contacts ->
                    contacts
                        .map { contact ->
                            ContactsUiState.Contact(
                                id = contact.id,
                                name = contact.completeName,
                                userAvatar = contact.userAvatar,
                            )
                        }
                }
                .flowOn(dispatcher.main)
                .collectLatest { contacts ->
                    uiState = ContactsUiState(
                        contacts = contacts,
                    )
                }
        }
    }
}
