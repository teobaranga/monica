package com.teobaranga.monica.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private val Context.settingsDataStore by preferencesDataStore(name = "settings")

    @Provides
    @Singleton
    fun providesSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.settingsDataStore
    }
}
