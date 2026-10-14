package com.teobaranga.monica.data

import androidx.room3.RoomDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.mockk.mockk

@ContributesTo(AppScope::class, replaces = [AndroidDatabaseBuilderComponent::class])
@BindingContainer
object FakeDatabaseBuilder {

    @Provides
    fun providesMonicaDatabaseBuilder(): RoomDatabase.Builder<MonicaDatabase> {
        return mockk()
    }
}
