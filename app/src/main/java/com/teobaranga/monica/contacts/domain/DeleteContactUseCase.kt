package com.teobaranga.monica.contacts.domain

import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DeleteContactUseCase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) {
    suspend operator fun invoke(contactId: Int) {
        return withContext(dispatcher.io) {
            contactRepository.deleteContact(contactId)
        }
    }
}
