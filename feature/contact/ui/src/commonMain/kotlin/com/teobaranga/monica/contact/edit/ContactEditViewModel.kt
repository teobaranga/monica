package com.teobaranga.monica.contact.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.toRoute
import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.contact.domain.UpsertContactUseCase
import com.teobaranga.monica.contact.edit.birthday.BirthdayMapper
import com.teobaranga.monica.contact.nav.ContactEditRoute
import com.teobaranga.monica.genders.domain.Gender
import com.teobaranga.monica.genders.domain.GetGendersUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val ID_CONTACT_UNDEFINED = -1

@AssistedInject
class ContactEditViewModel(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val contactRepository: ContactRepository,
    private val upsertContactUseCase: UpsertContactUseCase,
    private val getGendersUseCase: GetGendersUseCase,
    private val birthdayMapper: BirthdayMapper,
) : ViewModel() {

    private val navArgs = savedStateHandle.toRoute<ContactEditRoute>()

    val uiState = flow {
        val contact = navArgs.contactId?.let { contactId ->
            contactRepository.getContact(contactId).first()
        }
        val genders = getGendersUseCase().first()
        val state = if (contact == null) {
            getEmptyState(
                firstName = navArgs.contactName,
                genders = genders,
            )
        } else {
            ContactEditUiState.Loaded(
                id = contact.contactId,
                firstName = contact.firstName,
                lastName = contact.lastName,
                nickname = contact.nickname,
                initialGender = genders.firstOrNull { it.id == contact.genderId },
                genders = genders,
                initialBirthday = contact.birthdate?.let { birthdayMapper.toUi(it) },
            )
        }
        emit(state)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = ContactEditUiState.Loading,
        )

    fun onSave() {
        val uiState = uiState.value as? ContactEditUiState.Loaded ?: return
        viewModelScope.launch {
            upsertContactUseCase(
                contactId = navArgs.contactId,
                firstName = uiState.firstName.text,
                lastName = uiState.lastName.text,
                nickname = uiState.nickname.text,
                gender = uiState.gender,
                birthdate = uiState.birthday?.let { birthdayMapper.toDomain(it) },
            )
        }
    }

    fun onDelete() {
        navArgs.contactId?.let { contactId ->
            viewModelScope.launch {
                contactRepository.deleteContact(contactId)
            }
        }
    }

    private fun getEmptyState(firstName: String?, genders: List<Gender>): ContactEditUiState.Loaded {
        return ContactEditUiState.Loaded(
            id = ID_CONTACT_UNDEFINED,
            firstName = firstName.orEmpty(),
            lastName = null,
            nickname = null,
            initialGender = null,
            genders = genders,
            initialBirthday = null,
        )
    }

    @AssistedFactory
    @ViewModelAssistedFactoryKey(ContactEditViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory: ViewModelAssistedFactory {

        override fun create(extras: CreationExtras): ViewModel {
            return create(extras.createSavedStateHandle())
        }

        fun create(@Assisted savedStateHandle: SavedStateHandle): ContactEditViewModel
    }
}
