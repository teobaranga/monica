package com.teobaranga.monica.contact.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.toRoute
import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.contact.data.local.ContactEntity
import com.teobaranga.monica.contact.detail.section.ContactInfoContactSection
import com.teobaranga.monica.contact.detail.section.ContactInfoRelationshipsSection
import com.teobaranga.monica.contact.detail.section.activity.ContactInfoActivitiesSection
import com.teobaranga.monica.contact.detail.section.bio.ContactInfoBioSection
import com.teobaranga.monica.contact.edit.birthday.BirthdayMapper
import com.teobaranga.monica.contact.nav.ContactDetailRoute
import com.teobaranga.monica.genders.data.GenderRepository
import com.teobaranga.monica.useravatar.UserAvatar
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@AssistedInject
class ContactDetailViewModel(
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
    @ViewModelAssistedFactoryKey(ContactDetailViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory: ViewModelAssistedFactory {

        override fun create(extras: CreationExtras): ViewModel {
            return create(extras.createSavedStateHandle())
        }

        fun create(@Assisted savedStateHandle: SavedStateHandle): ContactDetailViewModel
    }
}
