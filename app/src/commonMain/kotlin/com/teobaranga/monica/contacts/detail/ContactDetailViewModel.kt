package com.teobaranga.monica.contacts.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.detail.activities.ui.ContactInfoActivitiesSection
import com.teobaranga.monica.contacts.detail.bio.ui.ContactInfoBioSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoContactSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoRelationshipsSection
import com.teobaranga.monica.contacts.edit.birthday.BirthdayMapper
import com.teobaranga.monica.genders.data.GenderRepository
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesViewModel(AppScope::class, assistedFactory = ContactDetailViewModel.Factory::class)
class ContactDetailViewModel internal constructor(
    contactRepository: ContactRepository,
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val genderRepository: GenderRepository,
    private val birthdayMapper: BirthdayMapper,
) : ViewModel() {

    private val contactDetailRoute = savedStateHandle.toRoute<ContactDetailRoute>()

    private val _effects = MutableSharedFlow<ContactDetailEffect>()
    val effects = _effects.asSharedFlow()

    val contact = contactRepository.getContact(contactDetailRoute.contactId)
        .catch {
            // TODO this is not great, better handle when a contact is deleted
            _effects.emit(ContactDetailEffect.Deleted)
        }
        .mapLatest { contact ->
            ContactDetail(
                id = contactDetailRoute.contactId,
                fullName = contact.getTitleName(),
                infoSections = listOf(
                    ContactInfoBioSection(
                        userAvatar = UserAvatar(
                            contactId = contactDetailRoute.contactId,
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
                        birthday = contact.birthdate?.let { birthdayMapper.toUi(it) },
                        gender = contact.genderId?.let { genderRepository.getById(it) }?.name,
                    ),
                    ContactInfoActivitiesSection(contactDetailRoute.contactId),
                    ContactInfoContactSection,
                    ContactInfoRelationshipsSection,
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
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
        operator fun invoke(savedStateHandle: SavedStateHandle): ContactDetailViewModel
    }
}
