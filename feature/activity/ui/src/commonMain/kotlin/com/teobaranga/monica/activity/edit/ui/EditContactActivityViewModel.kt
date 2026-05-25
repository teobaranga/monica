package com.teobaranga.monica.activity.edit.ui

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.toRoute
import com.teobaranga.monica.activity.data.ContactActivitiesRepository
import com.teobaranga.monica.activity.edit.domain.GetActivityUseCase
import com.teobaranga.monica.activity.nav.ContactActivityEditRoute
import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.contact.domain.SearchContactUseCase
import com.teobaranga.monica.contact.userAvatar
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@AssistedInject
class EditContactActivityViewModel(
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val getNowLocalDate: () -> LocalDate,
    private val contactRepository: ContactRepository,
    private val getActivity: GetActivityUseCase,
    private val searchContact: SearchContactUseCase,
    private val contactActivitiesRepository: ContactActivitiesRepository,
    private val mapContactToActivityParticipant: MapContactToActivityParticipant,
) : ViewModel() {

    private val contactActivityEditRoute = savedStateHandle.toRoute<ContactActivityEditRoute>()

    val contactId = contactActivityEditRoute.contactId
    val activityId = contactActivityEditRoute.activityId

    val uiState = flow {
        val uiState = EditContactActivityUiState.Loaded(
            initialDate = getNowLocalDate(),
        )
        when {
            activityId != null -> {
                val activity = getActivity(activityId)
                uiState.apply {
                    summary.setTextAndPlaceCursorAtEnd(activity.summary)
                    details.setTextAndPlaceCursorAtEnd(activity.details.orEmpty())
                    date = activity.date
                    participantsState.participants.addAll(activity.participants)
                }
            }

            contactId != null -> {
                val contact = contactRepository.getContact(contactId).first()
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
                activityId = activityId,
                title = uiState.summary.text.toString(),
                description = uiState.details.text.toString().takeUnless { it.isEmpty() },
                date = uiState.date,
                participants = uiState.participantsState.participants.map { it.contactId },
            )
        }
    }

    fun onDelete() {
        if (activityId == null) {
            return
        }
        viewModelScope.launch {
            contactActivitiesRepository.deleteActivity(activityId)
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
                                val results = searchContact(
                                    query = query,
                                    excludeContacts = participantsState.participants.map { it.contactId },
                                ).map { contacts ->
                                    contacts.map { contact ->
                                        mapContactToActivityParticipant(contact)
                                    }
                                }.first()
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
    @ViewModelAssistedFactoryKey(EditContactActivityViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory: ViewModelAssistedFactory {

        override fun create(extras: CreationExtras): ViewModel {
            return create(extras.createSavedStateHandle())
        }

        fun create(@Assisted savedStateHandle: SavedStateHandle): EditContactActivityViewModel
    }
}
