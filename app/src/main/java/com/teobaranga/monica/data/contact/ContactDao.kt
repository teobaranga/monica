package com.teobaranga.monica.data.contact

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query(value = "SELECT * FROM contacts")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query(
        value = """
        SELECT * FROM contacts
        WHERE id = :id
    """,
    )
    fun getContact(id: Int): Flow<ContactEntity>

    @Upsert
    suspend fun upsertContacts(entities: List<ContactEntity>)
}
