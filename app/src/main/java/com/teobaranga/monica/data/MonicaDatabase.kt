package com.teobaranga.monica.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teobaranga.monica.data.contact.ContactDao
import com.teobaranga.monica.data.contact.ContactEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        ContactEntity::class,
    ],
)
abstract class MonicaDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
