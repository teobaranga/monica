package com.teobaranga.monica.contacts.edit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.detail.toUiBirthday
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.contacts.ui.toDomainBirthday
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private const val ID_CONTACT_UNDEFINED = -1

@HiltViewModel(assistedFactory = ContactEditViewModel.Factory::class)
internal class ContactEditViewModel @AssistedInject constructor(
    @Assisted
    private val contactId: Int?,
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        if (contactId == null) {
            getEmptyState()
        } else {
            ContactEditUiState.Loading
        }
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher.io) {
            val contact = contactId?.let(contactRepository::getContact)?.firstOrNull()
            _uiState.value = if (contact == null) {
                getEmptyState()
            } else {
                ContactEditUiState.Loaded(
                    id = contact.contactId,
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    nickname = contact.nickname,
                    initialBirthday = contact.birthdate?.toUiBirthday(),
                )
            }
        }
    }

    fun onSave() {
        val uiState = _uiState.value as? ContactEditUiState.Loaded ?: return
        // TODO validation
        val firstname = uiState.firstName.getValidText()
        if (firstname == null) {
            // TODO error
            return
        }
        viewModelScope.launch(dispatcher.io) {
            contactRepository.upsertContact(
                contactId = contactId,
                firstName = firstname,
                lastName = uiState.lastName.getValidText(),
                nickname = uiState.nickname.getValidText(),
                birthdate = uiState.birthday?.toDomainBirthday(),
            )
        }
    }

    fun onDelete() {
        if (contactId == null) {
            return
        }
        viewModelScope.launch(dispatcher.io) {
            contactRepository.deleteContact(contactId)
        }
    }

    private fun getEmptyState(): ContactEditUiState.Loaded {
        return ContactEditUiState.Loaded(
            id = ID_CONTACT_UNDEFINED,
            firstName = "",
            lastName = null,
            nickname = null,
            initialBirthday = null,
        )
    }

    private fun TextFieldState.getValidText(): String? {
        return text.trim().takeIf { it.isNotEmpty() }?.toString()
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int?): ContactEditViewModel
    }
}
