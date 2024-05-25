package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.activities.data.ContactActivitiesRepository
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.list.userAvatar
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
    private val dispatcher: Dispatcher,
    contactRepository: ContactRepository,
    private val contactActivitiesRepository: ContactActivitiesRepository,
    @Assisted
    private val contactId: Int,
    @Assisted
    private val activityId: Int?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditContactActivityUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher.io) {
            val contact = contactRepository.getContact(contactId)
                .firstOrNull()
            if (contact == null) {
                // TODO handle
                return@launch
            }

            _uiState.value.participants.add(
                ActivityParticipant(
                    contactId = contact.contactId,
                    name = contact.completeName,
                    avatar = contact.userAvatar,
                ),
            )
        }
        if (activityId != null) {
            viewModelScope.launch(dispatcher.io) {
                val activity = contactActivitiesRepository.getActivity(activityId)
                    .firstOrNull()
                if (activity == null) {
                    // TODO handle
                    return@launch
                }

                _uiState.value.summary = TextFieldValue(activity.title)
                _uiState.value.details = TextFieldValue(activity.description.orEmpty())
                _uiState.value.date = activity.date
            }
        }
    }

    fun onSave() {
        if (activityId == null) {
            viewModelScope.launch(dispatcher.io) {
                val activity = _uiState.value
                contactActivitiesRepository.insertActivity(
                    title = activity.summary.text,
                    description = activity.details.text.takeUnless { it.isEmpty() },
                    date = activity.date,
                    participants = activity.participants.map { it.contactId },
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int, activityId: Int?): EditContactActivityViewModel
    }
}
