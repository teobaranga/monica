package com.teobaranga.monica.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.savedstate.serialization.SavedStateConfiguration
import com.teobaranga.monica.contacts.contacts
import com.teobaranga.monica.core.navigation.NavKeySerializer
import com.teobaranga.monica.dashboard.dashboard
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Builds the navigation configuration from the contributions of every feature.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object NavigationBindings {

    @Provides
    @IntoSet
    fun provideDashboardEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit = {
        dashboard()
    }

    @Provides
    @IntoSet
    fun provideContactsEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit = {
        contacts()
    }

    /**
     * Every destination of the app, each contributed by the feature that owns it.
     */
    @Provides
    @SingleIn(AppScope::class)
    fun provideEntryProvider(
        entryBuilders: Set<EntryProviderScope<NavKey>.() -> Unit>,
    ): (NavKey) -> NavEntry<NavKey> = entryProvider {
        entryBuilders.forEach { builder -> builder() }
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideSavedStateConfiguration(serializers: Set<NavKeySerializer>): SavedStateConfiguration {
        return SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    serializers.forEach { serializer ->
                        serializer.register(this)
                    }
                }
            }
        }
    }
}
