package com.teobaranga.monica.contacts.detail.activities.edit.domain

import com.teobaranga.monica.contacts.detail.activities.edit.ui.ActivityParticipant
import com.teobaranga.monica.contacts.domain.SearchContactUseCase
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchContactAsActivityParticipantUseCase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val searchContactUseCase: SearchContactUseCase,
    private val mapContactToActivityParticipant: MapContactToActivityParticipant,
) {
    suspend operator fun invoke(query: String, excludeContacts: List<Int> = emptyList()): List<ActivityParticipant> {
        return withContext(dispatcher.io) {
            searchContactUseCase(query, excludeContacts)
                .map { contact ->
                    mapContactToActivityParticipant(contact)
                }
        }
    }
}
