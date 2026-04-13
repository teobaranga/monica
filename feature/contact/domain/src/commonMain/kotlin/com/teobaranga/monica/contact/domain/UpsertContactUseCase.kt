package com.teobaranga.monica.contact.domain

import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.contact.data.local.ContactEntityBirthdate
import com.teobaranga.monica.genders.domain.Gender
import me.tatarka.inject.annotations.Inject

@Inject
class UpsertContactUseCase(
    private val contactRepository: ContactRepository,
) {
    suspend operator fun invoke(
        contactId: Int?,
        firstName: CharSequence,
        lastName: CharSequence?,
        nickname: CharSequence?,
        gender: Gender?,
        birthdate: ContactEntityBirthdate?,
    ) {
        // TODO validation
        val firstname = firstName.getValidText()
        if (firstname == null) {
            // TODO error
            return
        }
        return contactRepository.upsertContact(
            contactId = contactId,
            firstName = firstName.toString(),
            lastName = lastName?.getValidText(),
            nickname = nickname?.getValidText(),
            gender = gender,
            birthdate = birthdate,
        )
    }

    private fun CharSequence.getValidText(): String? {
        return trim().takeIf { it.isNotEmpty() }?.toString()
    }
}
