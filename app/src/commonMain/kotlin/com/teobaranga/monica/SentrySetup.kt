package com.teobaranga.monica

import io.sentry.kotlin.multiplatform.Sentry

expect val sentryDsn: String

fun initializeSentry() {
    Sentry.init { options ->
        options.dsn = sentryDsn
        options.debug = true
    }
}
