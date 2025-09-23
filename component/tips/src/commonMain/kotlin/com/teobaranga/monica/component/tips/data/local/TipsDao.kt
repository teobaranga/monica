package com.teobaranga.monica.component.tips.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
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
