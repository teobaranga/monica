package com.teobaranga.monica.photo.data.local

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class PhotoRepository(
    private val photoDao: PhotoDao,
) {

    fun getContactPhotos(contactId: Int): Flow<ContactPhotos> {
        return photoDao.getContactPhotos(contactId)
    }
}
