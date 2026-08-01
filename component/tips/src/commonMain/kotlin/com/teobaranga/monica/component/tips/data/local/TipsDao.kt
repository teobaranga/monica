package com.teobaranga.monica.component.tips.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.teobaranga.monica.component.tips.TipEntity

@Dao
abstract class TipsDao {

    @Query("SELECT * FROM tips WHERE id = :id")
    abstract suspend fun get(id: String): List<TipEntity>

    @Upsert
    abstract suspend fun upsert(entity: TipEntity)

    @Query("DELETE FROM tips")
    abstract suspend fun deleteAll()
}
