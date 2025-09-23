package com.teobaranga.monica.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.teobaranga.monica.activities.data.ContactActivitiesDao
import com.teobaranga.monica.activities.data.ContactActivityCrossRef
import com.teobaranga.monica.activities.data.ContactActivityEntity
import com.teobaranga.monica.component.tips.TipEntity
import com.teobaranga.monica.component.tips.di.TipsTableOwner
import com.teobaranga.monica.contacts.data.ContactDao
import com.teobaranga.monica.contacts.data.ContactEntity
import com.teobaranga.monica.data.photo.PhotoDao
import com.teobaranga.monica.data.photo.PhotoEntity
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.database.adapter.InstantAdapter
import com.teobaranga.monica.database.adapter.LocalDateAdapter
import com.teobaranga.monica.database.adapter.UuidAdapter
import com.teobaranga.monica.genders.data.GenderEntity
import com.teobaranga.monica.genders.data.GendersDao
import com.teobaranga.monica.journal.data.local.JournalDatabaseOwner
import com.teobaranga.monica.journal.data.local.JournalEntryEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        MeEntity::class,
        ContactEntity::class,
        ContactActivityEntity::class,
        ContactActivityCrossRef::class,
        PhotoEntity::class,
        JournalEntryEntity::class,
        GenderEntity::class,
        TipEntity::class,
    ],
)
@ConstructedBy(MonicaDatabaseConstructor::class)
@TypeConverters(
    InstantAdapter::class,
    LocalDateAdapter::class,
    UuidAdapter::class,
)
abstract class MonicaDatabase : RoomDatabase(), JournalDatabaseOwner, TipsTableOwner {
    abstract fun userDao(): UserDao
    abstract fun contactDao(): ContactDao
    abstract fun contactActivitiesDao(): ContactActivitiesDao
    abstract fun photoDao(): PhotoDao
    abstract fun gendersDao(): GendersDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object MonicaDatabaseConstructor : RoomDatabaseConstructor<MonicaDatabase> {
    override fun initialize(): MonicaDatabase
}
