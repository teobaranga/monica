package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.activities.data.ContactActivitiesRepository
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.list.userAvatar
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
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

    private val _uiState = MutableStateFlow<EditContactActivityUiState>(EditContactActivityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var participantSearchJob: Job? = null

    init {
        val getContact = contactRepository.getContact(contactId)
        val getActivity = if (activityId != null) {
            contactActivitiesRepository.getActivity(activityId)
        } else {
            flowOf(null)
        }
        viewModelScope.launch(dispatcher.io) {
            getContact
                .combine(getActivity) { contact, activityWithParticipants ->
                    EditContactActivityUiState.Loaded(
                        onParticipantSearch = ::onParticipantSearch,
                    ).apply {
                        participants.add(
                            ActivityParticipant(
                                contactId = contact.contactId,
                                name = contact.completeName,
                                avatar = contact.userAvatar,
                            ),
                        )
                        if (activityWithParticipants != null) {
                            summary = TextFieldValue(activityWithParticipants.activity.title)
                            details = TextFieldValue(activityWithParticipants.activity.description.orEmpty())
                            date = activityWithParticipants.activity.date
                            val activityParticipants = activityWithParticipants.participants
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
                            participants.addAll(activityParticipants)
                        }
                    }
                }
                .collectLatest { uiState ->
                    _uiState.value = uiState
                }
        }
    }

    fun onSave() {
        val uiState = getLoadedUiState() ?: return
        viewModelScope.launch(dispatcher.io) {
            contactActivitiesRepository.upsertActivity(
                activityId = activityId,
                title = uiState.summary.text,
                description = uiState.details.text.takeUnless { it.isEmpty() },
                date = uiState.date,
                participants = uiState.participants.map { it.contactId },
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
        val uiState = getLoadedUiState() ?: return
        uiState.participantResults.clear()
        participantSearchJob?.cancel()

        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return
        }

        participantSearchJob = viewModelScope.launch(dispatcher.io) {
            val results = contactRepository.searchContact(trimmedQuery)
            val existingParticipantIds = uiState.participants.map { it.contactId }.toSet()
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
                uiState.participantResults.addAll(participantResults)
            }
        }
    }

    private fun getLoadedUiState(): EditContactActivityUiState.Loaded? {
        return _uiState.value as? EditContactActivityUiState.Loaded
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int, activityId: Int?): EditContactActivityViewModel
    }
}
