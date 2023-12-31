package com.teobaranga.monica.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teobaranga.monica.contacts.data.ContactDao
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.data.photo.PhotoDao
import com.teobaranga.monica.data.photo.PhotoEntity
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.database.adapter.ZonedDateTimeAdapter
import com.teobaranga.monica.journal.database.JournalDao
import com.teobaranga.monica.journal.database.JournalEntryEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        MeEntity::class,
        ContactEntity::class,
        PhotoEntity::class,
        JournalEntryEntity::class,
    ],
)
@TypeConverters(
    ZonedDateTimeAdapter::class,
)
abstract class MonicaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun contactDao(): ContactDao
    abstract fun photoDao(): PhotoDao
    abstract fun journalDao(): JournalDao
}
