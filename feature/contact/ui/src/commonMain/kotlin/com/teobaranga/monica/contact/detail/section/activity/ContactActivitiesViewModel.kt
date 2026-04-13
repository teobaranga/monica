package com.teobaranga.monica.contact.detail.section.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.activity.data.ContactActivitiesRepository
import com.teobaranga.monica.activity.data.ContactActivityWithParticipants
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.time.Duration.Companion.seconds

@Inject
@ContributesViewModel(AppScope::class, assistedFactory = ContactActivitiesViewModel.Factory::class)
class ContactActivitiesViewModel(
    contactActivitiesRepository: ContactActivitiesRepository,
    dispatcher: Dispatcher,
    @Assisted
    private val contactId: Int,
) : ViewModel() {

    val contactActivities = contactActivitiesRepository.getActivities(contactId)
        .mapLatest { contactActivities ->
            val activities = contactActivities
                .map { contactActivityWithParticipants ->
                    contactActivityWithParticipants.toExternalModel(contactId)
                }
            if (activities.isEmpty()) {
                ContactActivitiesUiState.Empty
            } else {
                ContactActivitiesUiState.Loaded(activities)
            }
        }
        .onStart {
            viewModelScope.launch(dispatcher.io) {
                contactActivitiesRepository.syncActivities(contactId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = ContactActivitiesUiState.Loading,
        )

    fun ContactActivityWithParticipants.toExternalModel(contactId: Int): ContactActivity {
        return ContactActivity(
            id = activity.activityId,
            uuid = activity.uuid,
            title = activity.title,
            description = activity.description,
            date = activity.date,
            participants = participants
                .filterNot { it.contactId == contactId }
                .map {
                    Participant(
                        completeName = it.completeName,
                    )
                },
        )
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(contactId: Int): ContactActivitiesViewModel
    }
}
