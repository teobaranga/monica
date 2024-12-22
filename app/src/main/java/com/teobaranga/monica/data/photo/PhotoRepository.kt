package com.teobaranga.monica.data.photo

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class PhotoRepository(
    private val photoDao: PhotoDao,
) {
    fun getPhotos(contactId: Int): Flow<List<PhotoEntity>> {
        return photoDao.getPhotos(contactId)
    }
}
