package com.teobaranga.monica.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.data.user.UserRepository
import com.teobaranga.monica.user.userAvatar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ContactsViewModel @Inject constructor(
    userRepository: UserRepository,
    contactRepository: ContactRepository,
) : ViewModel() {

    val userAvatar = userRepository.me
        .mapLatest { me ->
            me.contact?.userAvatar ?: me.userAvatar
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val uiState = contactRepository.getContacts()
        .mapLatest { contacts ->
            val contacts = contacts
                .map { contact ->
                    ContactsUiState.Contact(
                        id = contact.id,
                        name = contact.completeName,
                        userAvatar = contact.userAvatar,
                    )
                }
            ContactsUiState(
                contacts = contacts,
            )
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = ContactsUiState(
                contacts = emptyList(),
            ),
            started = SharingStarted.WhileSubscribed(5_000),
        )
}
