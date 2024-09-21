package com.teobaranga.monica.contacts.domain

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchContactUseCase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) {
    suspend operator fun invoke(query: String, excludeContacts: List<Int> = emptyList()): List<ContactEntity> {
        return withContext(dispatcher.io) {
            val trimmedQuery = query.trim().takeIf { it.isNotBlank() }
                ?: return@withContext emptyList()
            contactRepository.searchContact(trimmedQuery, excludeContacts)
        }
    }
}
