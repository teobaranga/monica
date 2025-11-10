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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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
    private val getContact: GetContactUseCase,
    private val getActivity: GetActivityUseCase,
    private val searchContactAsActivityParticipant: SearchContactAsActivityParticipantUseCase,
    private val contactActivitiesRepository: ContactActivitiesRepository,
) : ViewModel() {

    val contactActivityEditRoute = savedStateHandle.toRoute<ContactActivityEditRoute>()

    val uiState = flow {
        val uiState = EditContactActivityUiState.Loaded(
            initialDate = getNowLocalDate(),
        )
        when {
            contactActivityEditRoute.activityId != null -> {
                val activity = getActivity(contactActivityEditRoute.activityId)
                uiState.apply {
                    summary.setTextAndPlaceCursorAtEnd(activity.summary)
                    details.setTextAndPlaceCursorAtEnd(activity.details.orEmpty())
                    date = activity.date
                    participantsState.participants.addAll(activity.participants)
                }
            }

            contactActivityEditRoute.contactId != null -> {
                val contact = getContact(contactActivityEditRoute.contactId)
                val contactParticipant = ActivityParticipant.Contact(
                    contactId = contact.contactId,
                    name = contact.completeName,
                    avatar = contact.userAvatar,
                )
                uiState.participantsState.participants.add(contactParticipant)
            }
        }
        emit(uiState)
    }.onEach {
        setupUi(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = EditContactActivityUiState.Loading,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
    )

    private var uiJob: Job? = null

    fun onSave() {
        val uiState = getLoadedUiState() ?: return
        viewModelScope.launch {
            contactActivitiesRepository.upsertActivity(
                activityId = contactActivityEditRoute.activityId,
                title = uiState.summary.text.toString(),
                description = uiState.details.text.toString().takeUnless { it.isEmpty() },
                date = uiState.date,
                participants = uiState.participantsState.participants.map { it.contactId },
            )
        }
    }

    fun onDelete() {
        if (contactActivityEditRoute.activityId == null) {
            return
        }
        viewModelScope.launch {
            contactActivitiesRepository.deleteActivity(contactActivityEditRoute.activityId)
        }
    }

    private fun getLoadedUiState(): EditContactActivityUiState.Loaded? {
        return uiState.value as? EditContactActivityUiState.Loaded
    }

    private fun setupUi(uiState: EditContactActivityUiState) {
        uiJob?.cancel()
        if (uiState is EditContactActivityUiState.Loaded) {
            uiJob = viewModelScope.launch {
                launch {
                    val participantsState = uiState.participantsState
                    snapshotFlow { participantsState.participantSearch.text.toString() }
                        .debounce(200.milliseconds)
                        .collect { query ->
                            if (query.isBlank()) {
                                participantsState.suggestions.clear()
                            } else {
                                val results = searchContactAsActivityParticipant(
                                    query = query,
                                    excludeContacts = participantsState.participants.map { it.contactId },
                                ).first()
                                val suggestions = if (results.isEmpty() && query.isNotBlank()) {
                                    listOf(ActivityParticipant.New(query))
                                } else {
                                    results
                                }
                                participantsState.suggestions.clear()
                                participantsState.suggestions.addAll(suggestions)
                            }
                        }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(savedStateHandle: SavedStateHandle): EditContactActivityViewModel
    }
}
