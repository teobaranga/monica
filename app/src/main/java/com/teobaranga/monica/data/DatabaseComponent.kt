package com.teobaranga.monica.data

import android.content.Context
import androidx.room.Room
import com.teobaranga.monica.configuration.domain.ConfigurationDataStore
import com.teobaranga.monica.configuration.domain.ConfigurationItem.ShouldClearDatabaseOnNextLaunch
import com.teobaranga.monica.inject.runtime.ApplicationContext
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface DatabaseComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun providesMonicaDatabase(
        @ApplicationContext context: Context,
        configurationDataStore: ConfigurationDataStore,
    ): MonicaDatabase {
        runBlocking {
            if (configurationDataStore.get(ShouldClearDatabaseOnNextLaunch)) {
                configurationDataStore.set(ShouldClearDatabaseOnNextLaunch, false)
                context.deleteDatabase("monica")
            }
        }
        return Room.databaseBuilder(context, MonicaDatabase::class.java, "monica")
            .build()
    }
}
