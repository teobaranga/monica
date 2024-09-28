package com.teobaranga.monica.contacts.detail.activities.edit.domain

import com.teobaranga.monica.contacts.detail.activities.edit.ui.ActivityParticipant
import com.teobaranga.monica.contacts.domain.SearchContactUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchContactAsActivityParticipantUseCase @Inject constructor(
    private val searchContactUseCase: SearchContactUseCase,
    private val mapContactToActivityParticipant: MapContactToActivityParticipant,
) {
    operator fun invoke(
        query: String,
        excludeContacts: List<Int> = emptyList(),
    ): Flow<List<ActivityParticipant>> {
        return searchContactUseCase(query, excludeContacts)
            .map { contacts ->
                contacts.map { contact ->
                    mapContactToActivityParticipant(contact)
                }
            }
    }
}
