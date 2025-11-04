package com.teobaranga.monica

import io.sentry.kotlin.multiplatform.Sentry

expect val sentryDsn: String

expect val sentryDebug: Boolean

fun initializeSentry() {
    Sentry.init { options ->
        options.dsn = sentryDsn
        options.debug = sentryDebug
    }
}
