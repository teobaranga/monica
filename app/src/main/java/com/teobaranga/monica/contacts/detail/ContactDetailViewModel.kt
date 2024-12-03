package com.teobaranga.monica.contacts.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.detail.activities.ui.ContactInfoActivitiesSection
import com.teobaranga.monica.contacts.detail.bio.ui.ContactInfoBioSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoContactSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoRelationshipsSection
import com.teobaranga.monica.genders.data.GenderRepository
import com.teobaranga.monica.inject.runtime.ContributesViewModel
import com.teobaranga.monica.ui.avatar.UserAvatar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@OptIn(ExperimentalCoroutinesApi::class)
@Inject
@ContributesViewModel(AppScope::class)
class ContactDetailViewModel internal constructor(
    contactRepository: ContactRepository,
    @Assisted
    private val contactId: Int,
    private val genderRepository: GenderRepository,
) : ViewModel() {

    private val _effects = MutableSharedFlow<ContactDetailEffect>()
    val effects = _effects.asSharedFlow()

    val contact = contactRepository.getContact(contactId)
        .catch {
            // TODO this is not great, better handle when a contact is deleted
            System.err.println(it)
            _effects.emit(ContactDetailEffect.Deleted)
        }
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
                        name = buildString {
                            append(contact.firstName)
                            if (contact.lastName != null) {
                                append(" ")
                                append(contact.lastName)
                            }
                        },
                        nickname = contact.nickname?.let { "($it)" },
                        birthday = contact.birthdate?.toUiBirthday(),
                        gender = contact.genderId?.let { genderRepository.getById(it) }?.name,
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
}
