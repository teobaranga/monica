package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

// For some reason, kotlin-inject (or anvil) doesn't generate the correct binding when using @ContributesBinding,
// possibly because the provided type is generic. Create this component for now and investigate this further.

@ContributesTo(
    scope = AppScope::class,
    replaces = [DataStoreComponent::class],
)
interface TestDataStoreComponent {

    @Provides
    fun dataStore(dataStore: TestDataStore): DataStore<Preferences> = dataStore
}
