package com.teobaranga.monica

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import javax.inject.Inject

class MonicaApp : Application(), ImageLoaderFactory, Configuration.Provider {

    internal val appComponent = AndroidAppComponent::class.create(this)

//    @Inject
//    lateinit var timberTrees: Set<@JvmSuppressWildcards Timber.Tree>

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

//        Timber.plant(*timberTrees.toTypedArray())

//        appComponent.workScheduler.schedule(SyncWorker.WORK_NAME)
    }

    override fun newImageLoader(): ImageLoader {
        return appComponent.imageLoader
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
}
