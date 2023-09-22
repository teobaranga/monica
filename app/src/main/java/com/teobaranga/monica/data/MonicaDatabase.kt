package com.teobaranga.monica.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teobaranga.monica.data.contact.ContactDao
import com.teobaranga.monica.data.contact.ContactEntity
import com.teobaranga.monica.data.photo.PhotoDao
import com.teobaranga.monica.data.photo.PhotoEntity
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.database.adapter.ZonedDateTimeAdapter

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        MeEntity::class,
        ContactEntity::class,
        PhotoEntity::class,
    ],
)
@TypeConverters(
    ZonedDateTimeAdapter::class,
)
abstract class MonicaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun contactDao(): ContactDao
    abstract fun photoDao(): PhotoDao
}
