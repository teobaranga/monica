package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.activities.data.ContactActivitiesRepository
import com.teobaranga.monica.contacts.detail.activities.edit.domain.GetActivityUseCase
import com.teobaranga.monica.contacts.detail.activities.edit.domain.SearchContactAsActivityParticipantUseCase
import com.teobaranga.monica.contacts.domain.GetContactUseCase
import com.teobaranga.monica.contacts.list.userAvatar
import com.teobaranga.monica.core.dispatcher.Dispatcher
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
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesViewModel(AppScope::class, assistedFactory = EditContactActivityViewModel.Factory::class)
class EditContactActivityViewModel internal constructor(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val getNowLocalDate: () -> LocalDate,
    private val dispatcher: Dispatcher,
    private val getContactUseCase: GetContactUseCase,
    private val getActivityUseCase: GetActivityUseCase,
    private val searchContactAsActivityParticipantUseCase: SearchContactAsActivityParticipantUseCase,
    private val contactActivitiesRepository: ContactActivitiesRepository,
) : ViewModel() {

    val contactActivityEditRoute = savedStateHandle.toRoute<ContactActivityEditRoute>()

    private val participantResults: StateFlow<List<ActivityParticipant>> = snapshotFlow {
        (uiState.value as? EditContactActivityUiState.Loaded)?.participantSearch?.text?.toString()
    }
        .filterNotNull()
        .debounce(200.milliseconds)
        .flatMapLatest { query ->
            val uiState = getLoadedUiState()
            if (query.isBlank() || uiState == null) {
                flowOf(emptyList())
            } else {
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
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    val uiState = flow {
        val uiState = EditContactActivityUiState.Loaded(
            initialDate = getNowLocalDate(),
            participantResults = participantResults,
        )
        if (contactActivityEditRoute.activityId != null) {
            val activity = getActivityUseCase(contactActivityEditRoute.activityId)
            uiState.apply {
                summary.setTextAndPlaceCursorAtEnd(activity.summary)
                details.setTextAndPlaceCursorAtEnd(activity.details.orEmpty())
                date = activity.date
                participants.addAll(activity.participants)
            }
        } else {
            if (contactActivityEditRoute.contactId != null) {
                val contact = getContactUseCase(contactActivityEditRoute.contactId)
                val contactParticipant = ActivityParticipant.Contact(
                    contactId = contact.contactId,
                    name = contact.completeName,
                    avatar = contact.userAvatar,
                )
                uiState.participants.add(contactParticipant)
            }
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
                activityId = contactActivityEditRoute.activityId,
                title = uiState.summary.text.toString(),
                description = uiState.details.text.toString().takeUnless { it.isEmpty() },
                date = uiState.date,
                participants = uiState.participants.map { it.contactId },
            )
        }
    }

    fun onDelete() {
        if (contactActivityEditRoute.activityId == null) {
            return
        }
        viewModelScope.launch(dispatcher.io) {
            contactActivitiesRepository.deleteActivity(contactActivityEditRoute.activityId)
        }
    }

    private fun getLoadedUiState(): EditContactActivityUiState.Loaded? {
        return uiState.value as? EditContactActivityUiState.Loaded
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): EditContactActivityViewModel
    }
}
