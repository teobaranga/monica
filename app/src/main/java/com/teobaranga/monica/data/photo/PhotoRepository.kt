package com.teobaranga.monica.data.photo

import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import okio.ByteString.Companion.decodeBase64
import timber.log.Timber
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class PhotoRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val photoApi: PhotoApi,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val photosMap = Collections.synchronizedMap(mutableMapOf<Int, List<Photo>>())

    private val _contactPhotos = MutableSharedFlow<Map<Int, List<Photo>>>(replay = 1)

    fun getPhotos(contactId: Int): Flow<List<Photo>> {
        scope.launch(dispatcher.io) {
            val photosResponse = photoApi.getPhotos(contactId)
            if (!photosResponse.isSuccessful) {
                Timber.w("Error while loading photos for contact %d: %s", contactId, photosResponse.errorBody())
                return@launch
            }
            val photos = requireNotNull(photosResponse.body())
            val photoList = photos.data
                .mapNotNull {
                    val data = it.data.split(',')[1].decodeBase64()?.asByteBuffer() ?: return@mapNotNull null
                    Photo(
                        fileName = it.fileName,
                        data = data
                    )
                }
            synchronized(photosMap) {
                photosMap[contactId] = photoList
            }
            _contactPhotos.emit(photosMap)
        }
        return _contactPhotos.mapLatest {
            it[contactId] ?: emptyList()
        }
    }
}
