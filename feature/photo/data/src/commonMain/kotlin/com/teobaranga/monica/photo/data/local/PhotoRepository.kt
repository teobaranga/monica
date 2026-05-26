package com.teobaranga.monica.photo.data.local

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow

@Inject
@SingleIn(AppScope::class)
class PhotoRepository(
    private val photoDao: PhotoDao,
) {

    fun getContactPhotos(contactId: Int): Flow<ContactPhotos> {
        return photoDao.getContactPhotos(contactId)
    }
}
