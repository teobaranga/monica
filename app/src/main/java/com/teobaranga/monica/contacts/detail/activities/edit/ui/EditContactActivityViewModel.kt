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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = EditContactActivityViewModel.Factory::class)
internal class EditContactActivityViewModel @AssistedInject constructor(
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
    private val contactActivitiesRepository: ContactActivitiesRepository,
    @Assisted
    private val contactId: Int,
    @Assisted
    private val activityId: Int?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditContactActivityUiState(
            onParticipantSearch = ::onParticipantSearch,
        ),
    )
    val uiState = _uiState.asStateFlow()

    private var participantSearchJob: Job? = null

    init {
        viewModelScope.launch(dispatcher.io) {
            val contact = contactRepository.getContact(contactId)
                .firstOrNull()
            if (contact == null) {
                // TODO handle
                return@launch
            }

            withContext(dispatcher.main) {
                _uiState.value.participants.add(
                    ActivityParticipant(
                        contactId = contact.contactId,
                        name = contact.completeName,
                        avatar = contact.userAvatar,
                    ),
                )
            }
        }
        if (activityId != null) {
            viewModelScope.launch(dispatcher.io) {
                val activityWithParticipants = contactActivitiesRepository.getActivity(activityId)
                    .firstOrNull()
                if (activityWithParticipants == null) {
                    // TODO handle
                    return@launch
                }
                val participants = activityWithParticipants.participants
                    .filter {
                        it.contactId != contactId
                    }
                    .map { contact ->
                        ActivityParticipant(
                            contactId = contact.contactId,
                            name = contact.completeName,
                            avatar = contact.userAvatar,
                        )
                    }

                withContext(dispatcher.main) {
                    _uiState.value.summary = TextFieldValue(activityWithParticipants.activity.title)
                    _uiState.value.details = TextFieldValue(activityWithParticipants.activity.description.orEmpty())
                    _uiState.value.date = activityWithParticipants.activity.date
                    _uiState.value.participants.addAll(participants)
                }
            }
        }
    }

    fun onSave() {
        val activity = _uiState.value
        viewModelScope.launch(dispatcher.io) {
            contactActivitiesRepository.upsertActivity(
                activityId = activityId,
                title = activity.summary.text,
                description = activity.details.text.takeUnless { it.isEmpty() },
                date = activity.date,
                participants = activity.participants.map { it.contactId },
            )
        }
    }

    fun onDelete() {
        if (activityId == null) {
            return
        }
        viewModelScope.launch(dispatcher.io) {
            contactActivitiesRepository.deleteActivity(activityId)
        }
    }

    private fun onParticipantSearch(query: String) {
        _uiState.value.participantResults.clear()
        participantSearchJob?.cancel()

        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return
        }

        participantSearchJob = viewModelScope.launch(dispatcher.io) {
            val results = contactRepository.searchContact(trimmedQuery)
            val existingParticipantIds = _uiState.value.participants.map { it.contactId }.toSet()
            val participantResults = results
                .filter {
                    it.contactId !in existingParticipantIds
                }
                .map { contact ->
                    ActivityParticipant(
                        contactId = contact.contactId,
                        name = contact.completeName,
                        avatar = contact.userAvatar,
                    )
                }
            withContext(dispatcher.main) {
                _uiState.value.participantResults.addAll(participantResults)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int, activityId: Int?): EditContactActivityViewModel
    }
}
