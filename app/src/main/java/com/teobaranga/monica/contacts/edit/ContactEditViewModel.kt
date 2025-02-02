package com.teobaranga.monica.contacts.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.generated.navArgs
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.contacts.detail.toUiBirthday
import com.teobaranga.monica.contacts.domain.DeleteContactUseCase
import com.teobaranga.monica.contacts.domain.GetContactUseCase
import com.teobaranga.monica.contacts.domain.UpsertContactUseCase
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.contacts.ui.toDomainBirthday
import com.teobaranga.monica.genders.domain.Gender
import com.teobaranga.monica.genders.domain.GetGendersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

private const val ID_CONTACT_UNDEFINED = -1

@Inject
@ContributesViewModel(AppScope::class)
class ContactEditViewModel internal constructor(
    @Assisted
    savedStateHandle: SavedStateHandle,
    private val getContactUseCase: GetContactUseCase,
    private val upsertContactUseCase: UpsertContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val getGendersUseCase: GetGendersUseCase,
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<ContactEditNavArgs>()

    val uiState = flow {
        val contact = if (navArgs.contactId != null) {
            getContactUseCase(navArgs.contactId)
        } else {
            null
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
                initialBirthday = contact.birthdate?.toUiBirthday(),
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
                birthdate = uiState.birthday?.toDomainBirthday(),
            )
        }
    }

    fun onDelete() {
        if (navArgs.contactId == null) {
            return
        }
        viewModelScope.launch {
            deleteContactUseCase(navArgs.contactId)
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
}
