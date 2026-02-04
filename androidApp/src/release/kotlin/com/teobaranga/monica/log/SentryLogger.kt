package com.teobaranga.monica.log

import com.diamondedge.logging.Logger
import io.sentry.Sentry
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(scope = AppScope::class, multibinding = true)
class SentryLogger : Logger {

    private val logger = Sentry.logger()

    override fun verbose(tag: String, msg: String) {
        logger.trace(msg)
    }

    override fun debug(tag: String, msg: String) {
        logger.debug(msg)
    }

    override fun info(tag: String, msg: String) {
        logger.info(msg)
    }

    override fun warn(tag: String, msg: String, t: Throwable?) {
        logger.warn(msg)
        if (t != null) {
            Sentry.captureException(t)
        }
    }

    override fun error(tag: String, msg: String, t: Throwable?) {
        logger.error(msg)
        if (t != null) {
            Sentry.captureException(t)
        }
    }

    override fun isLoggingVerbose(): Boolean = false

    override fun isLoggingDebug(): Boolean = false

    override fun isLoggingInfo(): Boolean = true

    override fun isLoggingWarning(): Boolean = true

    override fun isLoggingError(): Boolean = true
}
