package com.teobaranga.monica.log

import android.util.Log
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import timber.log.Timber

private const val KEY_MESSAGE = "message"

@ContributesBinding(scope = AppScope::class, multibinding = true)
class CrashlyticsTree @Inject constructor() : Timber.Tree() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Only log warnings and errors
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        if (t == null) {
            crashlytics.recordException(Exception(message))
        } else {
            val context = CustomKeysAndValues.Builder()
                .putString(KEY_MESSAGE, message)
                .build()
            crashlytics.recordException(t, context)
        }
    }
}
