package com.teobaranga.monica.configuration.domain

import android.content.Context
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import okio.Path
import okio.Path.Companion.toPath

private const val CONFIGURATION_DATASTORE_FILE_NAME = "datastore/configuration.preferences_pb"

@Inject
@ContributesBinding(AppScope::class)
class ConfigurationDataStorePathProviderImpl(
    @param:ApplicationContext
    private val context: Context,
): ConfigurationDataStorePathProvider {

    override fun invoke(): Path {
        return context.filesDir.resolve(CONFIGURATION_DATASTORE_FILE_NAME).absolutePath.toPath()
    }
}
