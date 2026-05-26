package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

// For some reason, kotlin-inject (or anvil) doesn't generate the correct binding when using @ContributesBinding,
// possibly because the provided type is generic. Create this component for now and investigate this further.

@ContributesTo(
    scope = AppScope::class,
    replaces = [SettingsDataStoreComponent::class],
)
interface TestSettingsDataStoreComponent {

    @Provides
    fun dataStore(dataStore: TestDataStore): DataStore<Preferences> = dataStore
}
