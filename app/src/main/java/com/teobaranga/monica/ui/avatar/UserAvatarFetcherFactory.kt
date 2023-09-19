package com.teobaranga.monica.ui.avatar

import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import com.teobaranga.monica.data.contact.ContactRepository
import kotlinx.coroutines.flow.first
import okio.Buffer
import okio.ByteString.Companion.decodeBase64
import javax.inject.Inject

class UserAvatarFetcherFactory @Inject constructor(
    private val contactRepository: ContactRepository,
) : Fetcher.Factory<UserAvatar> {

    /**
     * Fetcher implementation similar to `ByteBufferFetcher` where the data is fetched from our Room database.
     */
    private class UserAvatarFetcher(
        private val contactRepository: ContactRepository,
        private val data: UserAvatar,
        private val options: Options,
    ): Fetcher {
        override suspend fun fetch(): FetchResult? {
            val contactPhotos = contactRepository.getContactPhotos(data.contactId)
                .first {
                    it.avatarUrl == null || it.photos.isNotEmpty()
                }

            if (contactPhotos.avatarUrl == null || contactPhotos.photos.isEmpty()) {
                return null
            }

            val avatar = contactPhotos.photos
                .first {
                    it.fileName in contactPhotos.avatarUrl
                }

            val byteBuffer = avatar.data?.decodeBase64()?.asByteBuffer() ?: return null

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
        return UserAvatarFetcher(contactRepository, data, options)
    }
}
