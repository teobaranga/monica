package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditContactActivityViewModel.Factory::class)
internal class EditContactActivityViewModel @AssistedInject constructor(
    dispatcher: Dispatcher,
    contactRepository: ContactRepository,
    @Assisted
    private val contactId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditContactActivityUiState>(EditContactActivityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher.io) {
            val contact = contactRepository.getContact(contactId)
                .firstOrNull()
            if (contact == null) {
                // TODO handle
                return@launch
            }
            _uiState.value = EditContactActivityUiState.Loaded(
                participants = listOf(
                    ActivityParticipant(contact.completeName),
                ),
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int): EditContactActivityViewModel
    }
}
