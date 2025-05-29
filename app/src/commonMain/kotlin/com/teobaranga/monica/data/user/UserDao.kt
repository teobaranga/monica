package com.teobaranga.monica.data.user

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Transaction
    @Query("select * from me")
    fun getMe(): Flow<MeFullDetails?>

    @Upsert
    suspend fun upsertMe(entity: MeEntity)
}
