package com.teobaranga.monica.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.teobaranga.monica.core.navigation.NavKeySerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Builds the [SavedStateConfiguration] from every [NavKeySerializer] contributed into the graph.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object NavigationBindings {

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
