package com.teobaranga.monica.configuration.domain

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

private const val DATASTORE_FILE_NAME = "configuration.preferences_pb"

@Inject
@ContributesBinding(AppScope::class)
class ConfigurationDataStorePathProviderImpl : ConfigurationDataStorePathProvider {

    @OptIn(ExperimentalForeignApi::class)
    override fun invoke(): Path {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )

        return (requireNotNull(documentDirectory).path + "/$DATASTORE_FILE_NAME").toPath()
    }
}
