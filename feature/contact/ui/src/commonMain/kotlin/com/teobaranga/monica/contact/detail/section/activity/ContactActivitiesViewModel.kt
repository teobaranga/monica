package com.teobaranga.monica.contact.detail.section.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.activity.data.ContactActivitiesRepository
import com.teobaranga.monica.activity.data.ContactActivityWithParticipants
import com.teobaranga.monica.core.dispatcher.Dispatcher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AssistedInject
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
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory: ManualViewModelAssistedFactory {
        fun create(contactId: Int): ContactActivitiesViewModel
    }
}
