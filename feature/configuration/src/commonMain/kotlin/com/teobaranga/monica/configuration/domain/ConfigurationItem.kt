package com.teobaranga.monica.configuration.domain

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

sealed interface ConfigurationItem<T> {

    val key: Preferences.Key<T>

    val default: T

    data object ShouldClearDatabaseOnNextLaunch : ConfigurationItem<Boolean> {
        override val key = booleanPreferencesKey("should_clear_database_on_next_launch")
        override val default: Boolean = false
    }
}
