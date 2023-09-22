package com.teobaranga.monica.contacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.data.contact.ContactRepository
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    var uiState by mutableStateOf<ContactsUiState?>(null)

    init {
        viewModelScope.launch(dispatcher.io) {
            contactRepository.getContacts()
                .collectLatest {
                    val contacts = it
                        .map {
                            ContactsUiState.Contact(
                                id = it.id,
                                name = buildString {
                                    append(it.firstName)
                                    if (it.lastName != null) {
                                        append(" ")
                                        append(it.lastName)
                                    }
                                },
                                userAvatar = UserAvatar(
                                    contactId = it.id,
                                    initials = it.initials,
                                    color = it.avatarColor,
                                    avatarUrl = it.avatarUrl,
                                ),
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
