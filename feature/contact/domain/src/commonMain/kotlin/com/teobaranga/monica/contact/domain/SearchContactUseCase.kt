package com.teobaranga.monica.contact.domain

import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.contact.data.local.ContactEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import me.tatarka.inject.annotations.Inject

@Inject
class SearchContactUseCase(
    private val contactRepository: ContactRepository,
) {
    operator fun invoke(query: String, excludeContacts: List<Int> = emptyList()): Flow<List<ContactEntity>> {
        val trimmedQuery = query.trim().takeIf { it.isNotBlank() }
            ?: return emptyFlow()
        return contactRepository.searchContact(trimmedQuery, excludeContacts)
    }
}
