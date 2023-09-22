package com.teobaranga.monica.data.photo

import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private var needsSync: Boolean = true

    fun syncPhotos() {
        if (!needsSync) {
            return
        }
        needsSync = false
        scope.launch(dispatcher.io) {
            val photosResponse = photoApi.getPhotos()
                .onFailure {
                    Timber.e("Error while loading photos: %s", this)
                }
                .getOrNull() ?: return@launch
            val photoList = photosResponse.data
                .map {
                    PhotoEntity(
                        id = it.id,
                        fileName = it.fileName,
                        data = it.data.split(',').last(),
                        contactId = it.contact.id,
                    )
                }
            photoDao.upsertPhotos(photoList)
        }
    }

    fun getPhotos(contactId: Int): Flow<List<PhotoEntity>> {
        syncPhotos()
        return photoDao.getPhotos(contactId)
    }
}
