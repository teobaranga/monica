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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    var userUiState by mutableStateOf<UserAvatar?>(null)

    var uiState by mutableStateOf<ContactsUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            userRepository.me
                .collectLatest { me ->
                    val avatar = me.contact?.userAvatar ?: me.userAvatar
                    withContext(dispatcher.main) {
                        userUiState = avatar
                    }
                }
        }
        viewModelScope.launch(dispatcher.io) {
            contactRepository.getContacts()
                .collectLatest {
                    val contacts = it
                        .map { contact ->
                            ContactsUiState.Contact(
                                id = contact.id,
                                name = buildString {
                                    append(contact.firstName)
                                    if (contact.lastName != null) {
                                        append(" ")
                                        append(contact.lastName)
                                    }
                                },
                                userAvatar = contact.userAvatar,
                            )
                        }
                    withContext(dispatcher.main) {
                        uiState = ContactsUiState(
                            contacts = contacts,
                        )
                    }
                }
        }
    }
}
