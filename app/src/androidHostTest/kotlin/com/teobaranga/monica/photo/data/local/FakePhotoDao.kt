package com.teobaranga.monica.photo.data.local

import com.teobaranga.monica.data.DaosComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class, replaces = [DaosComponent::class])
class FakePhotoDao: PhotoDao {
    override fun getContactPhotos(contactId: Int): Flow<ContactPhotos> {
        TODO("Not yet implemented")
    }

    override fun getPhotoIds(): Flow<List<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertPhotos(entities: List<PhotoEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entityIds: List<Int>) {
        TODO("Not yet implemented")
    }
}
