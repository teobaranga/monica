package com.teobaranga.monica.data.user

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Transaction
    @Query("select * from me")
    fun getMe(): Flow<MeFullDetails?>

    @Upsert
    suspend fun upsertMe(entity: MeEntity)
}
