package com.teobaranga.monica.data.photo

import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.contacts.data.ContactPhotosResponse
import com.teobaranga.monica.data.sync.Synchronizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoSynchronizer @Inject constructor(
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao,
) : Synchronizer {

    override val syncState = MutableStateFlow(Synchronizer.State.IDLE)

    override suspend fun sync() {
        if (!isSyncEnabled) {
            return
        }

        syncState.value = Synchronizer.State.REFRESHING

        // Keep track of removed photos, start with the full database first
        val removedIds = photoDao.getPhotoIds().first().toMutableSet()

        var nextPage: Int? = 1
        while (nextPage != null) {
            val photosResponse = photoApi.getPhotos(page = nextPage)
                .onFailure {
                    Timber.w("Error while loading photos: %s", this)
                }
                .getOrElse {
                    syncState.value = Synchronizer.State.IDLE
                    return
                }
            val photoEntities = photosResponse.data
                .map {
                    it.toEntity()
                }

            photoDao.upsertPhotos(photoEntities)

            photosResponse.meta.run {
                nextPage = if (currentPage != lastPage) {
                    currentPage + 1
                } else {
                    null
                }
            }

            // Reduce the list of entries to be removed based on the entries previously inserted
            removedIds -= photoEntities.map { it.id }.toSet()
        }

        photoDao.delete(removedIds.toList())

        syncState.value = Synchronizer.State.IDLE

        isSyncEnabled = false
    }

    private fun ContactPhotosResponse.Photo.toEntity(): PhotoEntity {
        return PhotoEntity(
            id = id,
            fileName = fileName,
            data = data.split(',').last(),
            contactId = contact.id,
        )
    }

    companion object {

        private var isSyncEnabled = true
    }
}
