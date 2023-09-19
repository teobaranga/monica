package com.teobaranga.monica.ui.avatar

import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import com.teobaranga.monica.data.photo.PhotoRepository
import kotlinx.coroutines.flow.first
import okio.Buffer
import okio.ByteString.Companion.decodeBase64
import javax.inject.Inject

class UserAvatarFetcherFactory @Inject constructor(
    private val photoRepository: PhotoRepository,
) : Fetcher.Factory<UserAvatar> {

    /**
     * Fetcher implementation similar to `ByteBufferFetcher` where the data is fetched from our Room database.
     */
    private class UserAvatarFetcher(
        private val photoRepository: PhotoRepository,
        private val data: UserAvatar,
        private val options: Options,
    ): Fetcher {
        override suspend fun fetch(): FetchResult? {
            // TODO fetch the actual avatar rather than just getting the first picture
            val photo = photoRepository.getPhotos(data.contactId)
                .first {
                    it.isNotEmpty()
                }.first()

            val byteBuffer = photo.data?.decodeBase64()?.asByteBuffer() ?: return null

            val source = try {
                Buffer().apply {
                    write(byteBuffer)
                }
            } finally {
                // Reset the position so we can read the byte buffer again.
                byteBuffer.position(0)
            }

            return SourceResult(
                source = ImageSource(
                    source = source,
                    context = options.context,
                ),
                mimeType = null,
                dataSource = DataSource.DISK,
            )
        }
    }

    override fun create(data: UserAvatar, options: Options, imageLoader: ImageLoader): Fetcher {
        return UserAvatarFetcher(photoRepository, data, options)
    }
}
