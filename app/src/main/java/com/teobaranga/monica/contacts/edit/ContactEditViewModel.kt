package com.teobaranga.monica.contacts.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.detail.toBirthday
import com.teobaranga.monica.contacts.edit.ui.ContactEditUiState
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ContactEditViewModel.Factory::class)
internal class ContactEditViewModel @AssistedInject constructor(
    @Assisted
    private val contactId: Int?,
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContactEditUiState>(ContactEditUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher.io) {
            val contact = if (contactId == null) {
                null
            } else {
                contactRepository.getContact(contactId).firstOrNull()
            }
            _uiState.value = if (contact == null) {
                getEmptyState()
            } else {
                ContactEditUiState.Loaded(
                    id = contact.contactId,
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    nickname = contact.nickname,
                    initialBirthday = contact.birthdate?.toBirthday(),
                )
            }
        }
    }

    fun onSave() {
        // TODO
    }

    fun onDelete() {
        // TODO
    }

    private fun getEmptyState(): ContactEditUiState.Loaded {
        return ContactEditUiState.Loaded(
            id = -1,
            firstName = "",
            lastName = null,
            nickname = null,
            initialBirthday = null,
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int?): ContactEditViewModel
    }
}
