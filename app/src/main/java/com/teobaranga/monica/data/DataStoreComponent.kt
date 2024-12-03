package com.teobaranga.monica.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.teobaranga.monica.inject.runtime.ApplicationContext
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

@ContributesTo(AppScope::class)
interface DataStoreComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun providesSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.settingsDataStore
    }
}
