package com.teobaranga.monica.data.photo

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDao,
) {
    fun getPhotos(contactId: Int): Flow<List<PhotoEntity>> {
        return photoDao.getPhotos(contactId)
    }
}
