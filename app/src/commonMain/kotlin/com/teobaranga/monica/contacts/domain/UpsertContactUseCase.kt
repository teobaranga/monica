package com.teobaranga.monica.contacts.domain

import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.genders.domain.Gender
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
internal class UpsertContactUseCase(
    private val dispatcher: Dispatcher,
    private val contactRepository: ContactRepository,
) {
    suspend operator fun invoke(
        contactId: Int?,
        firstName: CharSequence,
        lastName: CharSequence?,
        nickname: CharSequence?,
        gender: Gender?,
        birthdate: ContactEntity.Birthdate?,
    ) {
        return withContext(dispatcher.io) {
            // TODO validation
            val firstname = firstName.getValidText()
            if (firstname == null) {
                // TODO error
                return@withContext
            }
            contactRepository.upsertContact(
                contactId = contactId,
                firstName = firstName.toString(),
                lastName = lastName?.getValidText(),
                nickname = nickname?.getValidText(),
                gender = gender,
                birthdate = birthdate,
            )
        }
    }

    private fun CharSequence.getValidText(): String? {
        return trim().takeIf { it.isNotEmpty() }?.toString()
    }
}
