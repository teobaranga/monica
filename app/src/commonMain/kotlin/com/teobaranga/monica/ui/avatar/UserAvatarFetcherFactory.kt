package com.teobaranga.monica.ui.avatar

import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.useravatar.UserAvatar
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import okio.Buffer
import okio.ByteString.Companion.decodeBase64

@Inject
class UserAvatarFetcherFactory(
    private val contactRepository: ContactRepository,
) : Fetcher.Factory<UserAvatar> {

    /**
     * Fetcher implementation similar to `ByteBufferFetcher` where the data is fetched from our Room database.
     */
    private class UserAvatarFetcher(
        private val contactRepository: ContactRepository,
        private val data: UserAvatar,
        private val options: Options,
    ) : Fetcher {
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

            return SourceFetchResult(
                source = ImageSource(
                    source = source,
                    fileSystem = options.fileSystem,
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
