package com.teobaranga.monica.photo.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Transaction
    @Query("SELECT contactId, avatar_url FROM contacts WHERE contactId = :contactId")
    fun getContactPhotos(contactId: Int): Flow<ContactPhotos>

    @Query("SELECT id FROM photos")
    fun getPhotoIds(): Flow<List<Int>>

    @Upsert
    suspend fun upsertPhotos(entities: List<PhotoEntity>)

    @Query("DELETE FROM photos WHERE id in (:entityIds)")
    suspend fun delete(entityIds: List<Int>)
}
