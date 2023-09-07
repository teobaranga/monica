package com.teobaranga.monica

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MonicaApp : Application() {

    @Inject
    lateinit var timberTrees: Set<@JvmSuppressWildcards Timber.Tree>

    override fun onCreate() {
        super.onCreate()

        Timber.plant(*timberTrees.toTypedArray())
    }
}
