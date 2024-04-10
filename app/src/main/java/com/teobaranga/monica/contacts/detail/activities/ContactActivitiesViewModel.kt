package com.teobaranga.monica.contacts.detail.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.activities.data.ContactActivitiesSynchronizer
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.util.coroutines.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = ContactActivitiesViewModel.Factory::class)
internal class ContactActivitiesViewModel @AssistedInject constructor(
    contactRepository: ContactRepository,
    dispatcher: Dispatcher,
    @Assisted
    private val contactId: Int,
    private val contactActivitiesSynchronizerFactory: ContactActivitiesSynchronizer.Factory,
) : ViewModel() {

    val contactActivities = contactRepository.getContactActivities(contactId)
        .mapLatest { contactActivities ->
            contactActivities
                .map { contactActivityEntity ->
                    val participants = contactRepository.getContacts(emptyList()).first()
                    contactActivityEntity.toExternalModel(participants)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    init {
        viewModelScope.launch(dispatcher.io) {
            contactActivitiesSynchronizerFactory.create(contactId)
                .sync()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: Int): ContactActivitiesViewModel
    }
}
