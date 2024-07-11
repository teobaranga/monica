package com.teobaranga.monica.contacts.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.detail.activities.ui.ContactInfoActivitiesSection
import com.teobaranga.monica.contacts.detail.bio.ui.ContactInfoBioSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoContactSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoRelationshipsSection
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
                id = contactId,
                fullName = contact.getTitleName(),
                infoSections = listOf(
                    ContactInfoBioSection(
                        userAvatar = UserAvatar(
                            contactId = contactId,
                            initials = contact.initials,
                            color = contact.avatar.color,
                            avatarUrl = contact.avatar.url,
                        ),
                        fullName = contact.completeName,
                        birthday = contact.birthdate?.toUiBirthday(),
                    ),
                    ContactInfoActivitiesSection(contactId),
                    ContactInfoContactSection,
                    ContactInfoRelationshipsSection,
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private fun ContactEntity.getTitleName(): String {
        return buildString {
            append(firstName)
            if (!nickname.isNullOrBlank()) {
                append(" ($nickname)")
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int): ContactDetailViewModel
    }
}
