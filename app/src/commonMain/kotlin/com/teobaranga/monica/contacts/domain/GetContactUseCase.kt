package com.teobaranga.monica.contacts.domain

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetContactUseCase(
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) {
    suspend operator fun invoke(contactId: Int): ContactEntity {
        return withContext(dispatcher.io) {
            contactRepository.getContact(contactId).first()
        }
    }
}
