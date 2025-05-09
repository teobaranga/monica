package com.teobaranga.monica.log

import com.diamondedge.logging.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(scope = AppScope::class, multibinding = true)
class CrashlyticsLogger : Logger {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun verbose(tag: String, msg: String) {
        crashlytics.log(msg)
    }

    override fun debug(tag: String, msg: String) {
        crashlytics.log(msg)
    }

    override fun info(tag: String, msg: String) {
        crashlytics.log(msg)
    }

    override fun warn(tag: String, msg: String, t: Throwable?) {
        crashlytics.log(msg)
        if (t != null) {
            crashlytics.recordException(t)
        }
    }

    override fun error(tag: String, msg: String, t: Throwable?) {
        crashlytics.log(msg)
        if (t != null) {
            crashlytics.recordException(t)
        }
    }

    override fun isLoggingVerbose(): Boolean = false

    override fun isLoggingDebug(): Boolean = false

    override fun isLoggingInfo(): Boolean = true

    override fun isLoggingWarning(): Boolean = true

    override fun isLoggingError(): Boolean = true
}
