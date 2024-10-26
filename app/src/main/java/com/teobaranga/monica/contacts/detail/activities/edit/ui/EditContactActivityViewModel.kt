package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.activities.data.ContactActivitiesRepository
import com.teobaranga.monica.contacts.detail.activities.edit.domain.GetActivityUseCase
import com.teobaranga.monica.contacts.detail.activities.edit.domain.SearchContactAsActivityParticipantUseCase
import com.teobaranga.monica.contacts.domain.GetContactUseCase
import com.teobaranga.monica.contacts.list.userAvatar
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = EditContactActivityViewModel.Factory::class)
internal class EditContactActivityViewModel @AssistedInject constructor(
    private val dispatcher: Dispatcher,
    private val getContactUseCase: GetContactUseCase,
    private val getActivityUseCase: GetActivityUseCase,
    private val searchContactAsActivityParticipantUseCase: SearchContactAsActivityParticipantUseCase,
    private val contactActivitiesRepository: ContactActivitiesRepository,
    @Assisted
    private val contactId: Int,
    @Assisted
    private val activityId: Int?,
) : ViewModel() {

    private val participantQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private val participantResults: StateFlow<List<ActivityParticipant>> = snapshotFlow {
        (uiState.value as? EditContactActivityUiState.Loaded)?.participantSearch?.text?.toString()
    }
        .filterNotNull()
        .debounce(200.milliseconds)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                return@flatMapLatest flowOf(emptyList())
            }
            val uiState = getLoadedUiState() ?: return@flatMapLatest flowOf(emptyList())
            val results = searchContactAsActivityParticipantUseCase(
                query = query,
                excludeContacts = uiState.participants.map { it.contactId },
            )
            results
                .map { results ->
                    if (results.isEmpty() && query.isNotBlank()) {
                        listOf(ActivityParticipant.New(query))
                    } else {
                        results
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    val uiState: StateFlow<EditContactActivityUiState> = flow {
        val uiState = EditContactActivityUiState.Loaded(
            participantResults = participantResults,
        )
        if (activityId != null) {
            val activity = getActivityUseCase(activityId)
            uiState.apply {
                summary.setTextAndPlaceCursorAtEnd(activity.summary)
                details.setTextAndPlaceCursorAtEnd(activity.details.orEmpty())
                date = activity.date
                participants.addAll(activity.participants)
            }
        } else {
            val contact = getContactUseCase(contactId)
            val contactParticipant = ActivityParticipant.Contact(
                contactId = contact.contactId,
                name = contact.completeName,
                avatar = contact.userAvatar,
            )
            uiState.participants.add(contactParticipant)
        }
        emit(uiState)
    }.stateIn(
        scope = viewModelScope,
        initialValue = EditContactActivityUiState.Loading,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
    )

    fun onSave() {
        val uiState = getLoadedUiState() ?: return
        viewModelScope.launch(dispatcher.io) {
            contactActivitiesRepository.upsertActivity(
                activityId = activityId,
                title = uiState.summary.text.toString(),
                description = uiState.details.text.toString().takeUnless { it.isEmpty() },
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
        participantQuery.tryEmit(query)
    }

    private fun getLoadedUiState(): EditContactActivityUiState.Loaded? {
        return uiState.value as? EditContactActivityUiState.Loaded
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int, activityId: Int?): EditContactActivityViewModel
    }
}
