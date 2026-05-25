package com.teobaranga.monica.photo.data

import com.diamondedge.logging.logging
import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.teobaranga.monica.core.account.AccountListener
import com.teobaranga.monica.core.data.sync.Synchronizer
import com.teobaranga.monica.photo.data.local.PhotoDao
import com.teobaranga.monica.photo.data.local.PhotoEntity
import com.teobaranga.monica.photo.data.remote.ContactPhotosResponse
import com.teobaranga.monica.photo.data.remote.PhotoApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

@Inject
@SingleIn(AppScope::class)
@ContributesIntoSet(AppScope::class, binding<AccountListener>())
class PhotoSynchronizer(
    private val photoApi: PhotoApi,
    private val photoDao: PhotoDao,
) : Synchronizer, AccountListener {

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
                    log.e { "Error while loading photos: ${message()}" }
                }
                .getOrElse {
                    syncState.value = Synchronizer.State.IDLE
                    return
                }
            val photoEntities = photosResponse.data
                .mapNotNull {
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

    private fun ContactPhotosResponse.Photo.toEntity(): PhotoEntity? {
        // Ignore disconnected pictures
        if (contact == null) {
            return null
        }
        return PhotoEntity(
            id = id,
            fileName = fileName,
            data = data.split(',').last(),
            contactId = contact.id,
        )
    }

    override fun onSignedIn() {
        isSyncEnabled = true
    }

    companion object {

        private val log = logging()

        private var isSyncEnabled = true
    }
}
