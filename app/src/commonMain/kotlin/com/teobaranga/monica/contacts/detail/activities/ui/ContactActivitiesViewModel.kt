package com.teobaranga.monica.contacts.detail.activities.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.activities.data.ContactActivitiesRepository
import com.teobaranga.monica.activities.data.ContactActivitiesSynchronizer
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
    private val contactActivitiesSynchronizerFactory: (contactId: Int) -> ContactActivitiesSynchronizer,
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
                contactActivitiesSynchronizerFactory(contactId)
                    .sync()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = ContactActivitiesUiState.Loading,
        )

    @AssistedFactory
    interface Factory {
        operator fun invoke(contactId: Int): ContactActivitiesViewModel
    }
}
