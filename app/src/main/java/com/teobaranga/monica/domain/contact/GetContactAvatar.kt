package com.teobaranga.monica.domain.contact

import com.teobaranga.monica.data.contact.ContactRepository
import com.teobaranga.monica.data.photo.Photo
import com.teobaranga.monica.data.photo.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

interface GetContactAvatar {

    suspend operator fun invoke(contactId: Int): Flow<Photo>
}

class MonicaGetContactAvatar @Inject constructor(
    private val contactRepository: ContactRepository,
    private val photoRepository: PhotoRepository,
) : GetContactAvatar {

    override suspend fun invoke(contactId: Int): Flow<Photo> {
        return combine(
            contactRepository.getContact(contactId),
            photoRepository.getPhotos(contactId)
        ) { contact, photos ->
            photos
                .find {
                    it.fileName in contact.avatar.url.orEmpty()
                }
        }.filterNotNull()
    }
}
