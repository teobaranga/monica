package com.teobaranga.monica.genders.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GendersDao {

    @Query("SELECT * FROM genders")
    abstract fun getGenders(): Flow<List<GenderEntity>>

    @Query("SELECT * FROM genders WHERE genderId = :genderId")
    abstract suspend fun getGenderById(genderId: Int): GenderEntity?

    @Query("SELECT * FROM genders WHERE name = :name")
    abstract suspend fun getGenderByName(name: String): GenderEntity?

    @Upsert
    abstract suspend fun upsertGenders(entities: List<GenderEntity>)
}
