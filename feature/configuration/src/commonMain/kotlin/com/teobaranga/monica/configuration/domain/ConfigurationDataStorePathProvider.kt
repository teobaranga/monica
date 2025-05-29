package com.teobaranga.monica.configuration.domain

import okio.Path

interface ConfigurationDataStorePathProvider {

    operator fun invoke(): Path
}
