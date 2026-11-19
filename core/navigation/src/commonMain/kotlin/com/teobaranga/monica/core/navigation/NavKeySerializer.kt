package com.teobaranga.monica.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder

/**
 * Registers a [NavKey] with the polymorphic serialization used to persist the navigation state.
 *
 * Non-JVM targets cannot rely on reflection based serialization, so every route has to be
 * registered explicitly. Route owners contribute an implementation into the app graph right next
 * to the route itself, which keeps the serialization configuration in sync with the destinations
 * that actually exist.
 */
fun interface NavKeySerializer {

    fun register(builder: PolymorphicModuleBuilder<NavKey>)
}

/**
 * Creates a [NavKeySerializer] that registers [T] using [serializer].
 */
inline fun <reified T : NavKey> navKeySerializer(serializer: KSerializer<T>): NavKeySerializer =
    NavKeySerializer { builder ->
        builder.subclass(T::class, serializer)
    }
