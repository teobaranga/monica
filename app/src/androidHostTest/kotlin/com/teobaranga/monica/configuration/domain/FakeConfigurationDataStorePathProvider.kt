package com.teobaranga.monica.configuration.domain

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import okio.Path
import okio.Path.Companion.toPath

@Inject
@ContributesBinding(AppScope::class, replaces = [ConfigurationDataStorePathProviderImpl::class])
class FakeConfigurationDataStorePathProvider: ConfigurationDataStorePathProvider {

    override fun invoke(): Path {
        return ".".toPath()
    }
}
