package com.teobaranga.monica.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.teobaranga.monica.activity.data.ContactActivityCrossRef
import com.teobaranga.monica.activity.data.ContactActivityEntity
import com.teobaranga.monica.activity.data.di.ActivityTableOwner
import com.teobaranga.monica.component.tips.TipEntity
import com.teobaranga.monica.component.tips.di.TipsTableOwner
import com.teobaranga.monica.contact.data.di.ContactTableOwner
import com.teobaranga.monica.contact.data.local.ContactEntity
import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.database.adapter.InstantAdapter
import com.teobaranga.monica.database.adapter.LocalDateAdapter
import com.teobaranga.monica.database.adapter.UuidAdapter
import com.teobaranga.monica.genders.data.GenderEntity
import com.teobaranga.monica.genders.di.GenderTableOwner
import com.teobaranga.monica.journal.data.local.JournalDatabaseOwner
import com.teobaranga.monica.journal.data.local.JournalEntryEntity
import com.teobaranga.monica.photo.data.local.PhotoDao
import com.teobaranga.monica.photo.data.local.PhotoEntity

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
abstract class MonicaDatabase : RoomDatabase(),
    JournalDatabaseOwner,
    TipsTableOwner,
    ContactTableOwner,
    ActivityTableOwner,
    GenderTableOwner {
    abstract fun userDao(): UserDao
    abstract fun photoDao(): PhotoDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object MonicaDatabaseConstructor : RoomDatabaseConstructor<MonicaDatabase> {
    override fun initialize(): MonicaDatabase
}
