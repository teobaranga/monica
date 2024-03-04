package com.teobaranga.monica.contacts.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.ui.avatar.UserAvatar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = ContactDetailViewModel.Factory::class)
internal class ContactDetailViewModel @AssistedInject constructor(
    contactRepository: ContactRepository,
    @Assisted
    private val contactId: Int,
) : ViewModel() {

    val contact = contactRepository.getContact(contactId)
        .mapLatest { contact ->
            ContactDetail(
                fullName = contact.completeName,
                userAvatar = UserAvatar(
                    contactId = contactId,
                    initials = contact.initials,
                    color = contact.avatarColor,
                    avatarUrl = contact.avatarUrl,
                ),
                tabs = listOf(
                    "Personal",
                    "Contact",
                    "Work",
                    "Relationships",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int): ContactDetailViewModel
    }
}
