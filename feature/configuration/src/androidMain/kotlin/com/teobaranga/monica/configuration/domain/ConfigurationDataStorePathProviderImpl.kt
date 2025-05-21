package com.teobaranga.monica.configuration.domain

import android.content.Context
import com.teobaranga.monica.core.inject.ApplicationContext
import me.tatarka.inject.annotations.Inject
import okio.Path
import okio.Path.Companion.toPath
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

private const val CONFIGURATION_DATASTORE_FILE_NAME = "datastore/configuration.preferences_pb"

@Inject
@ContributesBinding(AppScope::class)
class ConfigurationDataStorePathProviderImpl(
    @ApplicationContext
    private val context: Context,
): ConfigurationDataStorePathProvider {

    override fun invoke(): Path {
        return context.filesDir.resolve(CONFIGURATION_DATASTORE_FILE_NAME).absolutePath.toPath()
    }
}
